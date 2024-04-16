package com.penny.service;

import com.penny.dao.BookingAvailabilityVoMapper;
import com.penny.dao.DiscountVoMapper;
import com.penny.dao.base.BookingOrderBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.bookingOrder.BookingOrderCreateRequest;
import com.penny.util.DateHelper;
import com.penny.vo.BookingAvailabilityVo;
import com.penny.vo.DiscountVo;
import com.penny.vo.base.BookingOrderBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final EcUserService ecUserService;

    private final DateHelper dateHelper;

    /**
     * 創建預訂訂單。
     *
     * @param request 預訂訂單創建請求
     * @throws FieldConflictException 如果請求中缺少必要的欄位或欄位相衝突
     * @throws ResourceNotFoundException 如果指定的資源未找到
     */
    public void createBookingOrder(BookingOrderCreateRequest request) {
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

        // 檢查日期格式
        if (!dateHelper.isValidDateString(request.getCheckinDate()) || !dateHelper.isValidDateString(request.getCheckoutDate())) {
            throw new FieldConflictException("checkinDate and checkoutDate should be in yyyy-mm-dd format");
        }

        // 檢查房源是否為已發布
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with propertyId %s not found".formatted(propertyId));
        }

        // 獲取預訂訂單基本資訊
        BookingOrderBaseVo bookingOrderBaseVo = bookingOrderMapper(request);
        bookingOrderBaseVo.setHostId(propertyBaseVo.getHostId());
        bookingOrderBaseVo.setCustomerId(ecUserService.getLoginUser().getEcUserId());

        // 解析預訂日期
        LocalDate checkinDate = dateHelper.parseDateString(request.getCheckinDate());
        LocalDate checkoutDate = dateHelper.parseDateString(request.getCheckoutDate());
        List<BookingAvailabilityVo> bookingDateList = bookingAvailabilityVoMapper.listByStartAndEndDate(propertyId, checkinDate, checkoutDate);

        // 檢查資料庫中的預訂日期是否足夠覆蓋指定日期範圍
        if (bookingDateList.size() < countDayDifference(checkinDate, checkoutDate)) {
            // 生成指定日期範圍內的連續日期列表
            List<LocalDate> bookingLocalDateList = generateDateRange(checkinDate, checkoutDate);
            // 找出資料庫缺少的預訂日期
            List<LocalDate> dateMissingFromDb = listMissingBookingDate(bookingLocalDateList, bookingDateList);
            // 拋出異常，指示找不到足夠日期進行預訂
            throw new ResourceNotFoundException("%s are not available for booking".formatted(dateMissingFromDb));
        }

        int weekendCount = 0;
        for(BookingAvailabilityVo bookingDate : bookingDateList) {
            // 檢查日期可預訂性
            boolean isBooked = bookingDate.getBookingAvailabilityStatus().equals("booked");
            if (isBooked) {
                throw new FieldConflictException("%s has been booked".formatted(bookingDate.getBookingAvailabilityDate()));
            }

            // 計算假日數量
            LocalDate currentDate = bookingDate.getBookingAvailabilityDate();
            if (dateHelper.isWeekend(currentDate)) weekendCount ++;
        }

        // 依照平日假日價格計算訂房費用
        int numOfBookingDates = bookingDateList.size();
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

        // 儲存訂單
        bookingOrderBaseVoMapper.insertSelective(bookingOrderBaseVo);

        // 更新預定日期狀態
        List<Long> bookingAvailabilityIdList = bookingDateList
                .stream()
                .map(BookingAvailabilityVo::getBookingAvailabilityId)
                .collect(Collectors.toList());
        bookingAvailabilityVoMapper.setStatusToBookingByPrimaryKeyList(bookingAvailabilityIdList);
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
                .filter(discount ->  discount.getDiscountIsActive()
                        && discount.getLeastNumOfBookingDays() <= numOfBookingDates)
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
        String discountUnit = discountVo.getDiscountUnit();

        return switch (discountUnit) {
            case "percentage" -> totalBeforeDiscount * discountValue / 100;
            case "dollar" -> discountValue;
            default -> 0;
        };
    }

    /**
     * 將預訂訂單創建請求映射為預訂訂單基本資訊。
     *
     * @param request 預訂訂單創建請求
     * @return 預訂訂單基本資訊
     */
    private BookingOrderBaseVo bookingOrderMapper(BookingOrderCreateRequest request) {
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
