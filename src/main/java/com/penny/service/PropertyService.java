package com.penny.service;

import com.penny.dao.*;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.CreatePropertyRequest;
import com.penny.request.SearchPropertyRequest;
import com.penny.vo.*;
import com.penny.vo.base.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final String DEFAULT_PROPERTY_TITLE = "無標題";

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
    public List<PropertyVo> listPublishedProperty(SearchPropertyRequest request) {
        // 從查詢參數中獲取可預定天數、開始預定日期字串和結束預定日期字串
        Integer numOfAvailableDays = request.getNumOfAvailableDays();
        String startAvailableDateString = request.getStartAvailableDateString();
        String endAvailableDateString = request.getEndAvailableDateString();

        // 檢查參數中是否存在衝突情況
        if (numOfAvailableDays != null && startAvailableDateString != null) {
            throw new FieldConflictException("numOfAvailableDays and startAvailableDateString cannot coexist");
        }

        // 如果存在開始預定日期但沒有結束預定日期，則拋出異常
        if (startAvailableDateString != null && endAvailableDateString == null) {
            throw new FieldConflictException("startAvailableDateString cannot exist without endAvailableDateString");
        }

        // 如果存在結束預定日期但沒有開始預定日期，則拋出異常
        if (startAvailableDateString == null && endAvailableDateString != null) {
            throw new FieldConflictException("startAvailableDateString cannot exist without endAvailableDateString");
        }

        // 檢查日期格式是否有效，若無效則拋出異常
        if((startAvailableDateString != null && endAvailableDateString != null) && (!isValidDateString(startAvailableDateString) || !isValidDateString(endAvailableDateString))) {
            throw new FieldConflictException("invalid startAvailableDateString and endAvailableDateString format");
        }

        // 初始化結果列表
        List<PropertyVo> resultList;

        // 根據情況執行不同的查詢操作並賦值給結果列表
        if (numOfAvailableDays != null) {
            resultList =  propertyVoMapper.listByNumOfAvailableDays(request);
        } else if (startAvailableDateString != null) {
            request.setStartAvailableDate(parseDateString(startAvailableDateString));
            request.setEndAvailableDate(parseDateString(endAvailableDateString));

            resultList =  propertyVoMapper.listByStartAndEndAvailableDate(request);
        } else  {
            resultList = propertyVoMapper.listByPropertyAttributes(request);
        }

        // 將結果列表中已發佈的房源過濾出來並返回
        return resultList
            .stream()
            .filter(PropertyVo::getIsPublished)
            .collect(Collectors.toList());
    }

    /**
     * 根據房源 ID 獲取房源基本資訊。
     *
     * @param propertyId 房源 ID
     * @return 房源基本資訊
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發佈，則拋出此異常
     */
    public PropertyBaseVo getPublishedProperty(Long propertyId) {
        // 根據房源 ID 查詢房源基本資訊
        PropertyBaseVo property = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property %s is not found".formatted(propertyId)));

        // 如果房源未發佈，則拋出異常
        if (!property.getIsPublished()) {
            throw new ResourceNotFoundException("property %s is not found".formatted(propertyId));
        }

        return property;
    }

    /**
     * 創建房源。
     *
     * @param createRequest 要用於創建房源的創建請求
     *
     */
    public void createProperty(CreatePropertyRequest createRequest){
        if (createRequest.getPropertyTitle() == null || createRequest.getPropertyTitle().isBlank()) {
            createRequest.setPropertyTitle(DEFAULT_PROPERTY_TITLE);
        }

        PropertyBaseVo propertyBaseVo = PropertyBaseVo
                .builder()
                .propertyDescription(createRequest.getPropertyDescription())
                .maxNumOfGuests(createRequest.getMaxNumOfGuests())
                .numOfBathrooms(createRequest.getNumOfBathrooms())
                .numOfBeds(createRequest.getNumOfBeds())
                .numOfBedrooms(createRequest.getNumOfBedrooms())
                .priceOnWeekdays(createRequest.getPriceOnWeekdays())
                .priceOnWeekends(createRequest.getPriceOnWeekends())
                .isPublished(createRequest.getIsPublished())
                .propertyMainSubTypeId(createRequest.getPropertyMainSubTypeId())
                .addressId(createRequest.getAddressId())
                .build();

        propertyBaseVoMapper.insertSelective(propertyBaseVo);
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

        return DATE_PATTERN.matcher(dateString).matches();
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
