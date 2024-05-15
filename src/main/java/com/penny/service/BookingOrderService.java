package com.penny.service;

import com.penny.dao.BookingCalendarVoMapper;
import com.penny.dao.BookingOrderVoMapper;
import com.penny.dao.DiscountVoMapper;
import com.penny.dao.base.BookingCalendarBaseVoMapper;
import com.penny.dao.base.BookingOrderBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.enums.BookingOrderPaymentStatusEnum;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.exception.AuthorizationException;
import com.penny.request.ConfirmPaymentRequest;
import com.penny.request.CreateBookingOrderRequest;
import com.penny.util.DateHelper;
import com.penny.vo.BookingCalendarVo;
import com.penny.vo.BookingOrderVo;
import com.penny.vo.DiscountVo;
import com.penny.vo.base.BookingCalendarBaseVo;
import com.penny.vo.base.BookingOrderBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingOrderService {
    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final BookingCalendarBaseVoMapper bookingCalendarBaseVoMapper;

    private final BookingCalendarVoMapper bookingCalendarVoMapper;

    private final DiscountVoMapper discountVoMapper;

    private final BookingOrderBaseVoMapper bookingOrderBaseVoMapper;

    private final BookingOrderVoMapper bookingOrderVoMapper;

    private final EcUserService ecUserService;

    private final DateHelper dateHelper;


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

        // 將 request 物件轉換為 bookingOrderBaseVo 物件
        BookingOrderBaseVo bookingOrderBaseVo = bookingOrderMapper(createRequest);
        LocalDate checkinDate = bookingOrderBaseVo.getCheckinDate();
        LocalDate checkoutDate = bookingOrderBaseVo.getCheckoutDate();

        // 檢查 checkin 日期是否早於 checkout 日期
        if(checkinDate.isAfter(checkoutDate)) {
            throw new RequestValidationException("checkinDate should be before checkoutDate");
        }

        // 檢查 checkin 日期是否是未來日期
        if (checkinDate.isBefore(LocalDate.now())){
            throw new RequestValidationException("checkinDate should be later than today");
        }

        // 獲取預訂訂單基本資訊
        bookingOrderBaseVo.setHostId(propertyBaseVo.getHostId());
        bookingOrderBaseVo.setCustomerId(ecUserService.getLoginUser().getEcUserId());

        // 檢查指定日期區間是否已被預定
        List<BookingCalendarVo> bookingCalendarVoList = bookingCalendarVoMapper.listByStartAndEndDate(propertyId, checkinDate, checkoutDate);
        if (!bookingCalendarVoList.isEmpty()) {
            throw new RequestValidationException("date range %s has been booked".formatted(
                    bookingCalendarVoList.stream().map(BookingCalendarVo::getBookingDate).toList())
            );
        }

        // 依照平日假日價格計算訂房費用
        List<LocalDate> requestBookingDateList = dateHelper.generateDateRange(checkinDate, checkoutDate);
        int weekendCount = 0;
        int weekdayCount = 0;
        for(LocalDate bookingDate : requestBookingDateList) {
            if (dateHelper.isWeekend(bookingDate)) {
                weekendCount ++;
                continue;
            }
            weekdayCount ++;
        }

        int totalBeforeDiscount = weekendCount * propertyBaseVo.getPriceOnWeekends()
                + weekdayCount * propertyBaseVo.getPriceOnWeekdays();

        // 找出適用的折扣，計算折扣後的費用
        int numOfBookingDates = requestBookingDateList.size();
        DiscountVo applicableDiscount = findApplicableDiscount(propertyId, numOfBookingDates);
        double discountValue = 0;
        if (applicableDiscount != null) {
            discountValue = calculateDiscountValue(applicableDiscount, totalBeforeDiscount);
            bookingOrderBaseVo.setDiscountId(applicableDiscount.getDiscountId());
        }
        BigDecimal totalAfterDiscount = BigDecimal.valueOf(Math.ceil(totalBeforeDiscount - discountValue));
        bookingOrderBaseVo.setOrderTotal(totalAfterDiscount);

        // 新增預定日期到預定日曆表
        for(LocalDate bookingDate : requestBookingDateList) {
            BookingCalendarBaseVo bookingCalendarBaseVo = BookingCalendarBaseVo
                    .builder()
                    .bookingDate(bookingDate)
                    .propertyId(propertyId)
                    .build();

            int insertCount = bookingCalendarBaseVoMapper.insertSelective(bookingCalendarBaseVo);
            if (insertCount != 1) throw new RequestValidationException("The specified dates have been booked");
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
     * 更新訂單狀態。
     *
     * @param confirmPaymentRequest 確認付款請求
     */
    @Transactional
    public void updateBookingOrderStatus(ConfirmPaymentRequest confirmPaymentRequest) {
        // 根據支付確認請求中的預訂訂單 ID 查詢相關的預訂訂單
        BookingOrderBaseVo bookingOrderBaseVo = bookingOrderBaseVoMapper.selectByPrimaryKey(confirmPaymentRequest.getBookingOrderId());

        // 如果支付成功
        if (confirmPaymentRequest.getIsPaymentSuccessful()) {
            // 設置預訂訂單的支付狀態為成功
            bookingOrderBaseVo.setPaymentStatus(BookingOrderPaymentStatusEnum.SUCCEED.getDisplayName());
        } else {
            // 如果支付失敗，設置預訂訂單的支付狀態為失敗
            bookingOrderBaseVo.setPaymentStatus(BookingOrderPaymentStatusEnum.FAIL.getDisplayName());

            // 根據預訂訂單的屬性 ID、入住日期和退房日期刪除對應的預訂日曆記錄
            bookingCalendarVoMapper.deleteByPropertyIdAndCheckinPeriod(bookingOrderBaseVo.getPropertyId(), bookingOrderBaseVo.getCheckinDate(), bookingOrderBaseVo.getCheckoutDate());
        }

        // 更新預訂訂單資訊
        bookingOrderBaseVoMapper.updateByPrimaryKey(bookingOrderBaseVo);
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
     * @param createRequest 預訂訂單創建請求
     * @return 預訂訂單基本資訊
     */
    private BookingOrderBaseVo bookingOrderMapper(CreateBookingOrderRequest createRequest) {
        return BookingOrderBaseVo
                .builder()
                .checkinDate(dateHelper.parseDateString(createRequest.getCheckinDateString()))
                .checkoutDate(dateHelper.parseDateString(createRequest.getCheckoutDateString()))
                .arrivalTime(createRequest.getArrivalTime())
                .guestName(createRequest.getGuestName())
                .guestEmail(createRequest.getGuestEmail())
                .guestPhone(createRequest.getGuestPhone())
                .guestMessage(createRequest.getGuestMessage())
                .propertyId(createRequest.getPropertyId())
                .build();
    }

}
