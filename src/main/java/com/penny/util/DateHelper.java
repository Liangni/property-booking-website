package com.penny.util;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Service
public class DateHelper {

    /**
     * 檢查日期字串是否符合指定的格式（yyyy-MM-dd）。
     *
     * @param dateString 要檢查的日期字串
     * @return 如果日期字串符合指定的格式，則返回 true；否則返回 false
     */
    public boolean isValidDateString(String dateString) {
        // 定義日期格式的正規表達式
        String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";

        // 創建 Pattern 物件
        Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);

        return DATE_PATTERN.matcher(dateString).matches();
    }

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
            throw new DateTimeParseException("日期字串無法解析為 LocalDate：" + dateString, dateString, 0);
        }
    }

    public boolean isWeekend(LocalDate date) {
        // 獲取給定日期的星期幾
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 檢查是否為星期六或星期日
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
