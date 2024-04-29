package com.penny.service;

import com.penny.dao.BookingAvailabilityVoMapper;
import com.penny.dao.BookingOrderVoMapper;
import com.penny.dao.DiscountVoMapper;
import com.penny.dao.base.BookingOrderBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.exception.UnauthorizedException;
import com.penny.request.CreateBookingOrderRequest;
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

    /**
     * 創建預訂訂單。
     *
     * @param request 預訂訂單創建請求
     * @throws FieldConflictException 如果請求中缺少必要的欄位或欄位相衝突
     * @throws ResourceNotFoundException 如果指定的資源未找到
     */
    @Transactional
    public void createBookingOrder(CreateBookingOrderRequest request) {
        Long propertyId = request.getPropertyId();

        // 檢查必要欄位
        if (propertyId == null) {
            throw new FieldConflictException("propertyId is required");
        }

        if (request.getCheckinDate() == null || request.getCheckoutDate() == null) {
            throw new FieldConflictException("checkinDate and checkoutDate are required");
        }

        if (request.getGuestName() == null) {
            throw new FieldConflictException("guestName is required");
        }

        if (request.getGuestEmail() == null) {
            throw new FieldConflictException("guestEmail is required");
        }

        if (request.getArrivalTime() == null) {
            throw new FieldConflictException("arrivalTime is required");
        }

        // 檢查房源是否為已發布
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with propertyId %s not found".formatted(propertyId));
        }

        // 檢查日期格式
        if (!dateHelper.isValidDateString(request.getCheckinDate()) || !dateHelper.isValidDateString(request.getCheckoutDate())) {
            throw new FieldConflictException("checkinDate and checkoutDate should be in yyyy-mm-dd format");
        }

        // 解析預訂日期
        LocalDate checkinDate = dateHelper.parseDateString(request.getCheckinDate());
        LocalDate checkoutDate = dateHelper.parseDateString(request.getCheckoutDate());

        // 檢查 checkin 日期是否早於 checkout 日期
        if(checkinDate.isAfter(checkoutDate)) {
            throw new FieldConflictException("checkinDate should be before checkoutDate");
        }

        // 檢查 checkin 日期是否是未來日期
        if (checkinDate.isBefore(LocalDate.now())){
            throw new FieldConflictException("checkinDate should be later than today");
        }

        // 獲取預訂訂單基本資訊
        BookingOrderBaseVo bookingOrderBaseVo = bookingOrderMapper(request);
        bookingOrderBaseVo.setHostId(propertyBaseVo.getHostId());
        bookingOrderBaseVo.setCustomerId(ecUserService.getLoginUser().getEcUserId());

        // 檢查資料庫中的預訂日期是否足夠覆蓋指定日期範圍
        List<BookingAvailabilityVo> bookingAvailabilityVoList = bookingAvailabilityVoMapper.listByStartAndEndDate(propertyId, checkinDate, checkoutDate);
        if (bookingAvailabilityVoList.size() < countDayDifference(checkinDate, checkoutDate)) {
            // 生成指定日期範圍內的連續日期列表
            List<LocalDate> bookingLocalDateList = generateDateRange(checkinDate, checkoutDate);
            // 找出資料庫缺少的預訂日期
            List<LocalDate> dateMissingFromDb = listMissingBookingDate(bookingLocalDateList, bookingAvailabilityVoList);
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
            throw new FieldConflictException("date range %s has been booked".formatted(bookedDateList));
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
            throw new FieldConflictException("The specified dates have been booked");
        }

        // 儲存訂單
        bookingOrderBaseVoMapper.insertSelective(bookingOrderBaseVo);
    }

    /**
     * 根據用戶類型取得使用者的預訂訂單列表。
     *
     * @param ecUserId 使用者ID
     * @param isHost 是否為房東
     * @return 符合條件的預訂訂單列表
     */
    public List<BookingOrderVo> getBookingOrders(Long ecUserId, Boolean isHost) {
        Long loginEcUserId = ecUserService.getLoginUser().getEcUserId();

        if (!ecUserId.equals(loginEcUserId)) {
            throw new UnauthorizedException("login user is not authorized to the resource");
        }

        if (isHost) {
            return bookingOrderVoMapper.listByHostId(loginEcUserId);
        }
        return bookingOrderVoMapper.listByCustomerId(loginEcUserId);
    }

    /**
     * 計算兩個日期之間的天數差異。
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 兩個日期之間的天數差異
     */
    private int countDayDifference(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 生成一段連續日期的列表，從起始日期到結束日期（包括起始日期和結束日期）。
     *
     * @param startDate 起始日期
     * @param endDate   結束日期
     * @return 一段連續日期的列表
     */
    private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dateList;
    }

    /**
     * 找出連續日期列表中缺少的預訂日期。
     *
     * @param consecutiveDateList      連續日期列表
     * @param bookingAvailabilityVoList 已預訂日期列表
     * @return 缺少的預訂日期列表
     */
    private List<LocalDate> listMissingBookingDate(List<LocalDate> consecutiveDateList, List<BookingAvailabilityVo> bookingAvailabilityVoList) {
        Set<LocalDate> dateSet = new HashSet<>(
                bookingAvailabilityVoList
                        .stream()
                        .map(BookingAvailabilityVo::getBookingAvailabilityDate)
                        .toList()
        );
        List<LocalDate> missingBookingDateList = new ArrayList<>();

        for (LocalDate date : consecutiveDateList) {
            if (!dateSet.contains(date)) {
                missingBookingDateList.add(date);
            }
        }

        return missingBookingDateList;
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
     * @param request 預訂訂單創建請求
     * @return 預訂訂單基本資訊
     */
    private BookingOrderBaseVo bookingOrderMapper(CreateBookingOrderRequest request) {
        return BookingOrderBaseVo
                .builder()
                .checkinDate(dateHelper.parseDateString(request.getCheckinDate()))
                .checkoutDate(dateHelper.parseDateString(request.getCheckoutDate()))
                .arrivalTime(request.getArrivalTime())
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .guestMessage(request.getGuestMessage())
                .propertyId(request.getPropertyId())
                .build();
    }

}
