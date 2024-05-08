package com.penny.util;

import com.penny.vo.BookingAvailabilityVo;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DateHelper {

    /**
     * 將日期字串解析為 LocalDate 物件。
     *
     * @param dateString 日期字串，格式為 "yyyy-mm-dd"
     * @return 解析後的 LocalDate 物件
     * @throws DateTimeParseException 如果日期字串無法解析為 LocalDate，則拋出此異常
     */
    public LocalDate parseDateString(String dateString) {
        if (dateString == null) return null;

        // 定義日期格式模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // 將日期字串解析為 LocalDate
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // 如果日期字串無法解析，則拋出 DateTimeParseException 異常
            throw new DateTimeParseException("Unable to parse String to LocalDate：" + dateString, dateString, 0);
        }
    }

    public boolean isWeekend(LocalDate date) {
        // 獲取給定日期的星期幾
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 檢查是否為星期六或星期日
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 計算兩個日期之間的天數差異。
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 兩個日期之間的天數差異
     */
    public int countDayDifference(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 生成一段連續日期的列表，從起始日期到結束日期（包括起始日期和結束日期）。
     *
     * @param startDate 起始日期
     * @param endDate   結束日期
     * @return 一段連續日期的列表
     */
    public List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
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
    public List<LocalDate> listMissingBookingDate(List<LocalDate> consecutiveDateList, List<BookingAvailabilityVo> bookingAvailabilityVoList) {
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
}
