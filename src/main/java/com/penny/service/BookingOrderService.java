package com.penny.service;

import com.penny.dao.BookingAvailabilityVoMapper;
import com.penny.dao.BookingOrderVoMapper;
import com.penny.dao.DiscountVoMapper;
import com.penny.dao.base.BookingOrderBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.exception.AuthorizationException;
import com.penny.request.CreateBookingOrderRequest;
import com.penny.request.CreateBookingOrderRequestDTO;
import com.penny.request.CreateBookingOrderRequestDTOMapper;
import com.penny.util.DateHelper;
import com.penny.vo.BookingAvailabilityVo;
import com.penny.vo.BookingOrderVo;
import com.penny.vo.DiscountVo;
import com.penny.vo.base.BookingOrderBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingOrderService {
    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final BookingAvailabilityVoMapper bookingAvailabilityVoMapper;

    private final DiscountVoMapper discountVoMapper;

    private final BookingOrderBaseVoMapper bookingOrderBaseVoMapper;

    private final BookingOrderVoMapper bookingOrderVoMapper;

    private final EcUserService ecUserService;

    private final DateHelper dateHelper;
    
    private final CreateBookingOrderRequestDTOMapper createBookingOrderRequestDTOMapper;

    /**
     * 創建預訂訂單。
     *
     * @param createRequest 預訂訂單創建請求
     * @throws RequestValidationException 如果請求中缺少必要的欄位或欄位相衝突
     * @throws ResourceNotFoundException 如果指定的資源未找到
     */
    @Transactional
    public void createBookingOrder(CreateBookingOrderRequest createRequest) {
        Long propertyId = createRequest.getPropertyId();

        // 檢查房源是否為已發布
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with propertyId %s not found".formatted(propertyId));
        }

        CreateBookingOrderRequestDTO createRequestDTO = createBookingOrderRequestDTOMapper.apply(createRequest);
        LocalDate checkinDate = createRequestDTO.getCheckinDate();
        LocalDate checkoutDate = createRequestDTO.getCheckoutDate();

        // 檢查 checkin 日期是否早於 checkout 日期
        if(checkinDate.isAfter(checkoutDate)) {
            throw new RequestValidationException("checkinDate should be before checkoutDate");
        }

        // 檢查 checkin 日期是否是未來日期
        if (checkinDate.isBefore(LocalDate.now())){
            throw new RequestValidationException("checkinDate should be later than today");
        }

        // 獲取預訂訂單基本資訊
        BookingOrderBaseVo bookingOrderBaseVo = bookingOrderMapper(createRequestDTO);
        bookingOrderBaseVo.setHostId(propertyBaseVo.getHostId());
        bookingOrderBaseVo.setCustomerId(ecUserService.getLoginUser().getEcUserId());

        // 檢查資料庫中的預訂日期是否足夠覆蓋指定日期範圍
        List<BookingAvailabilityVo> bookingAvailabilityVoList = bookingAvailabilityVoMapper.listByStartAndEndDate(propertyId, checkinDate, checkoutDate);
        if (bookingAvailabilityVoList.size() < dateHelper.countDayDifference(checkinDate, checkoutDate)) {
            // 生成指定日期範圍內的連續日期列表
            List<LocalDate> bookingLocalDateList = dateHelper.generateDateRange(checkinDate, checkoutDate);
            // 找出資料庫缺少的預訂日期
            List<LocalDate> dateMissingFromDb = dateHelper.listMissingBookingDate(bookingLocalDateList, bookingAvailabilityVoList);
            // 拋出異常，指示找不到足夠日期進行預訂
            throw new ResourceNotFoundException("%s are not available for booking".formatted(dateMissingFromDb));
        }

        int weekendCount = 0;
        List<LocalDate> bookedDateList = new ArrayList<>();
        for(BookingAvailabilityVo bookingDate : bookingAvailabilityVoList) {
            // 檢查日期可預訂性
            boolean isBooked = bookingDate.getBookingAvailabilityStatus().equals("booked");
            if (isBooked) bookedDateList.add(bookingDate.getBookingAvailabilityDate());

            // 計算假日數量
            LocalDate currentDate = bookingDate.getBookingAvailabilityDate();
            if (dateHelper.isWeekend(currentDate)) weekendCount ++;
        }

        // 若 checkin 至 checkout 期間有日期是已預訂，則拋出錯誤
        if (!bookedDateList.isEmpty()) {
            throw new RequestValidationException("date range %s has been booked".formatted(bookedDateList));
        }

        // 依照平日假日價格計算訂房費用
        int numOfBookingDates = bookingAvailabilityVoList.size();
        int weekdayCount = numOfBookingDates - weekendCount;
        int totalBeforeDiscount = weekendCount * propertyBaseVo.getPriceOnWeekends()
                + weekdayCount * propertyBaseVo.getPriceOnWeekdays();

        // 找出適用的折扣，計算折扣後的費用
        DiscountVo applicableDiscount = findApplicableDiscount(propertyId, numOfBookingDates);
        double discountValue = 0;
        if (applicableDiscount != null) {
            discountValue = calculateDiscountValue(applicableDiscount, totalBeforeDiscount);
            bookingOrderBaseVo.setDiscountId(applicableDiscount.getDiscountId());
        }
        BigDecimal totalAfterDiscount = BigDecimal.valueOf(Math.ceil(totalBeforeDiscount - discountValue));
        bookingOrderBaseVo.setOrderTotal(totalAfterDiscount);

        // 提取 bookingAvailabilityVoList 中每個物件的 bookingAvailabilityId 到新的列表中
        List<Long> bookingAvailabilityIdList = bookingAvailabilityVoList
                .stream()
                .map(BookingAvailabilityVo::getBookingAvailabilityId)
                .collect(Collectors.toList());

        // 更新 bookingAvailabilityIdList 中對應的預定日期的狀態
        int updateCount = bookingAvailabilityVoMapper.setStatusToBookingByPrimaryKeyList(bookingAvailabilityIdList);

        // 檢查更新的數量是否與預期的數量相同，如果不同則拋出異常
        if (updateCount != bookingAvailabilityVoList.size()) {
            throw new RequestValidationException("The specified dates have been booked");
        }

        // 儲存訂單
        bookingOrderBaseVoMapper.insertSelective(bookingOrderBaseVo);
    }

    /**
     * 根據使用者類型取得使用者的預訂訂單列表。
     *
     * @param ecUserId 使用者ID
     * @param isHost 是否為房東
     * @return 符合條件的預訂訂單列表
     */
    public List<BookingOrderVo> getBookingOrders(Long ecUserId, Boolean isHost) {
        Long loginEcUserId = ecUserService.getLoginUser().getEcUserId();

        if (!ecUserId.equals(loginEcUserId)) {
            throw new AuthorizationException("login user is not authorized to the resource");
        }

        if (isHost) {
            return bookingOrderVoMapper.listByHostId(loginEcUserId);
        }
        return bookingOrderVoMapper.listByCustomerId(loginEcUserId);
    }

    /**
     * 找出適用的折扣。
     *
     * @param propertyId        房源 ID
     * @param numOfBookingDates 預訂日期數量
     * @return 適用的折扣
     */
    private DiscountVo findApplicableDiscount(Long propertyId, int numOfBookingDates) {
        List<DiscountVo> discountVoList = discountVoMapper.listByPropertyId(propertyId)
                .stream()
                .filter(discount ->  discount.getLeastNumOfBookingDays() <= numOfBookingDates)
                .sorted(Comparator.comparing(DiscountVo::getLeastNumOfBookingDays))
                .toList();

        if (discountVoList.isEmpty()) return null;

        return discountVoList.get(discountVoList.size() - 1);
    }

    /**
     * 計算折扣金額。
     *
     * @param discountVo          折扣資訊
     * @param totalBeforeDiscount 折扣前總價
     * @return 折扣金額
     */
    private double calculateDiscountValue(DiscountVo discountVo, int totalBeforeDiscount) {
        Double discountValue = discountVo.getDiscountValue();
        return totalBeforeDiscount * discountValue / 100;
    }

    /**
     * 將預訂訂單創建請求映射為預訂訂單基本資訊。
     *
     * @param createRequestDTO 預訂訂單創建請求
     * @return 預訂訂單基本資訊
     */
    private BookingOrderBaseVo bookingOrderMapper(CreateBookingOrderRequestDTO createRequestDTO) {
        return BookingOrderBaseVo
                .builder()
                .checkinDate(createRequestDTO.getCheckinDate())
                .checkoutDate(createRequestDTO.getCheckoutDate())
                .arrivalTime(createRequestDTO.getArrivalTime())
                .guestName(createRequestDTO.getGuestName())
                .guestEmail(createRequestDTO.getGuestEmail())
                .guestPhone(createRequestDTO.getGuestPhone())
                .guestMessage(createRequestDTO.getGuestMessage())
                .propertyId(createRequestDTO.getPropertyId())
                .build();
    }

}
