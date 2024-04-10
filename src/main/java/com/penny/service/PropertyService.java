package com.penny.service;

import com.amazonaws.HttpMethod;
import com.penny.dao.*;
import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.dao.base.PictureDtBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.PropertyPictureBaseVoMapper;
import com.penny.enums.PictureDtSize;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.property.PropertySearchParam;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.s3.S3Buckets;
import com.penny.s3.S3Service;
import com.penny.vo.*;
import com.penny.vo.base.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyVoMapper propertyVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;


    /**
     * 根據給定的搜尋參數，獲取房源資訊。
     *
     * @param request 房源搜尋參數
     * @return 符合搜尋條件的房源列表
     * @throws FieldConflictException 如果 numOfAvailableDays 和 startAvailableDateString 同時存在
     * @throws FieldConflictException 如果 startAvailableDateString 存在而 endAvailableDateString 不存在
     * @throws FieldConflictException 如果 startAvailableDateString 或 endAvailableDateString 格式錯誤
     */
    public List<PropertyVo> getProperties(PropertySearchParam request) {
        Integer numOfAvailableDays = request.getNumOfAvailableDays();
        String startAvailableDateString = request.getStartAvailableDateString();
        String endAvailableDateString = request.getEndAvailableDateString();

        if (numOfAvailableDays != null && startAvailableDateString != null) {
            throw new FieldConflictException("numOfAvailableDays and startAvailableDateString cannot coexist");
        }

        if (startAvailableDateString != null && endAvailableDateString == null) {
            throw new FieldConflictException("startAvailableDateString cannot exist without endAvailableDateString");
        }

        if(!isValidDateString(startAvailableDateString) || !isValidDateString(endAvailableDateString)) {
            throw new FieldConflictException("invalid startAvailableDateString and endAvailableDateString format");
        }

        if (numOfAvailableDays != null) {
            return propertyVoMapper.listByNumOfAvailableDays(request);
        }

        if (startAvailableDateString != null) {
            request.setStartAvailableDate(parseDateString(startAvailableDateString));
            request.setEndAvailableDate(parseDateString(endAvailableDateString));

            return propertyVoMapper.listByStartAndEndAvailableDate(request);
        }

        return propertyVoMapper.listByPropertyAttributes(request);
    }

    /**
     * 根據房源 ID 獲取房源基本資訊。
     *
     * @param propertyId 房源 ID
     * @return 房源基本資訊
     * @throws ResourceNotFoundException 如果找不到對應的房源
     */
    public PropertyBaseVo getProperty(Long propertyId) {
        return Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property %s is not found".formatted(propertyId)));
    }

    /**
     * 檢查日期字串是否符合指定的格式（yyyy-MM-dd）。
     *
     * @param dateString 要檢查的日期字串
     * @return 如果日期字串符合指定的格式，則返回 true；否則返回 false
     */
    private boolean isValidDateString(String dateString) {
        // 定義日期格式的正規表達式
        String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";

        // 創建 Pattern 物件
        Pattern  DATE_PATTERN = Pattern.compile(DATE_REGEX);

        return !DATE_PATTERN.matcher(dateString).matches();
    }

    /**
     * 將日期字串解析為 LocalDate 物件。
     *
     * @param dateString 日期字串，格式為 "yyyy-MM-dd"
     * @return 解析後的 LocalDate 物件
     * @throws DateTimeParseException 如果日期字串無法解析為 LocalDate，則拋出此異常
     */
    private LocalDate parseDateString(String dateString) {
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

}
