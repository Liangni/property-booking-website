package com.penny.service;

import com.penny.dao.PropertyVoMapper;
import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.exception.FieldConflictException;
import com.penny.exception.UnknownException;
import com.penny.request.property.PropertySearchRequest;
import com.penny.util.Paginator;
import com.penny.vo.PropertyVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PropertyService {

    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);
    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_LIMIT = 10;

    private final PropertyVoMapper propertyVoMapper;

    private final Paginator paginator;

    private final List<String> allowedReturnFiledList = List.of(
            "propertyId","propertyTitle", "propertyDescription", "propertyMaxNumOfGuests", "numOfBedrooms",
            "numOfBeds", "numOfBathrooms", "priceOnWeekdays", "priceOnWeekends", "isPublished",
            "averageRating", "propertyShareTypeId", "addressId", "address", "district", "startAvailableDate",
            "endAvailableDate"
    );

    @Autowired
    public PropertyService(PropertyVoMapper propertyVoMapper, Paginator paginator){
        this.propertyVoMapper = propertyVoMapper;
        this.paginator = paginator;
    }

    public Map<String, Object> getPropertiesByFilter(PropertySearchRequest propertySearchRequest) {
        // 確認 filterMap 中 numOfAvailableDays 屬性與 startAvailableDate 或 endAvailableDate 屬性不同時存在
        Map<String, Object> requestFilterMap = propertySearchRequest.getFilterMap();

        if (requestFilterMap != null) {
            boolean isNumOfAvailableDaysPresentWithValueNotNull = Optional.ofNullable(requestFilterMap.get("numOfAvailableDays")).isPresent();
            boolean isStartAvailableDatePresentWithValueNotNull = Optional.ofNullable(requestFilterMap.get("startAvailableDate")).isPresent();
            boolean isEndAvailableDatePresentWithValueNotNull =Optional.ofNullable(requestFilterMap.get("endAvailableDate")).isPresent();

            // 檢查是否 numOfAvailableDays 和 startAvailableDate 同時存在
            boolean isNumOfAvailableDaysAndStartAvailableDatePresent =
                    isNumOfAvailableDaysPresentWithValueNotNull && isStartAvailableDatePresentWithValueNotNull;

            // 檢查是否 numOfAvailableDays 和 endAvailableDate 同時存在
            boolean isNumOfAvailableDaysAndEndAvailableDatePresent =
                    isNumOfAvailableDaysPresentWithValueNotNull && isEndAvailableDatePresentWithValueNotNull;

            // 如果有其中一種情況存在，則拋出異常
            if (isNumOfAvailableDaysAndStartAvailableDatePresent || isNumOfAvailableDaysAndEndAvailableDatePresent) {
                throw new FieldConflictException("filter numOfAvailableDays cannot co-exist with startAvailableDate or endAvailableDate");
            }

            // 確認 filterMap 中的 startAvailableDate 和 endAvailableDate 成對出現
            // 檢查是否只有其中一個存在且值不為 null
            boolean isOnlyOneDatePresentWithValueNotNull =
                    (isStartAvailableDatePresentWithValueNotNull && !isEndAvailableDatePresentWithValueNotNull) ||
                            (!isStartAvailableDatePresentWithValueNotNull && isEndAvailableDatePresentWithValueNotNull);

            if (isOnlyOneDatePresentWithValueNotNull) {
                throw new FieldConflictException("filter startAvailableDate and endAvailableDate should exist together");
            }

            // 若 filterMap 中的 startAvailableDate 和 endAvailableDate 同時存在，確認 startAvailableDate 不晚於 endAvailableDate
            boolean isStartAndEndAvailableDatePresent = isStartAvailableDatePresentWithValueNotNull && isEndAvailableDatePresentWithValueNotNull;
            if (isStartAndEndAvailableDatePresent) {
                // 定義日期格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 解析日期字符串為 LocalDate
                LocalDate startAvailableDate = LocalDate.parse(String.valueOf(requestFilterMap.get("startAvailableDate")), formatter);
                LocalDate endAvailableDate = LocalDate.parse(String.valueOf(requestFilterMap.get("endAvailableDate")), formatter);

                if (startAvailableDate.isAfter(endAvailableDate)) {
                    throw new FieldConflictException("filter startAvailableDate cannot be later than endAvailableDate");
                }

                requestFilterMap.put("startAvailableDate", startAvailableDate);
                requestFilterMap.put("endAvailableDate", endAvailableDate);
            }
        }

        // 確認 returnFieldList 的元素在定義的範圍內
        List<String> requestReturnFieldList = Optional.ofNullable(propertySearchRequest.getReturnFieldList()).orElse(new ArrayList<>());
        if (!requestReturnFieldList.isEmpty()) {
            for (String field: requestReturnFieldList) {
                if (allowedReturnFiledList.contains(field)) continue;
                throw new FieldConflictException(String.format("return field [%s] is not allowed", field));
            }
        }

        // 確認 page and limit
        if (propertySearchRequest.getPage() != null && propertySearchRequest.getPage() <= 0) {
            throw new FieldConflictException("page cannot be less than or equals to 0");
        }

        int page = Optional.ofNullable(propertySearchRequest.getPage()).orElse(DEFAULT_PAGE);
        int limit = Optional.ofNullable(propertySearchRequest.getLimit()).orElse(DEFAULT_LIMIT);
        int offset = paginator.calculateOffset(page, limit);

        // 找出房源
        SelectPropertyParam selectPropertyParam = SelectPropertyParam
                .builder()
                .filterMap(Optional.ofNullable(requestFilterMap).orElse(new HashMap<>()))
                .returnFieldList(requestReturnFieldList)
                .sortMap(Optional.ofNullable(propertySearchRequest.getSortMap()).orElse(new HashMap<>()))
                .offset(offset)
                .limit(limit)
                .build();
        List<PropertyVo> propertyVoList = propertyVoMapper.listByFilter(selectPropertyParam);

        List<Map<String, Object>> leanPropertyMapList = new ArrayList<>();
        for (PropertyVo propertyVo: propertyVoList) {
            Map<String, Object> propertyMap = new HashMap<>();

            for (String field: requestReturnFieldList) {
                // address
                if (field.equals("address")) {
                    propertyMap.put("apartmentFloor", propertyVo.getApartmentFloor());
                    propertyMap.put("street", propertyVo.getStreet());
                    propertyMap.put("districtId", propertyVo.getAdminAreaLevel3DistrictId());
                    continue;
                }

                // district
                if (field.equals("district")) {
                    propertyMap.put("districtId", propertyVo.getDistrictId());
                    propertyMap.put("districtName", propertyVo.getDistrictName());
                    propertyMap.put("parentDistrictId", propertyVo.getParentDistrictId());
                    propertyMap.put("parentDistrictName", propertyVo.getParentDistrictName());
                    continue;
                }

                // 組合 getter 方法名稱
                String getterMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);

                try {
                    // 使用反射獲取屬性值
                    Method getterMethod = propertyVo.getClass().getMethod(getterMethodName);

                    // 調用 getter 方法獲取屬性值
                    Object value = getterMethod.invoke(propertyVo);
                    propertyMap.put(field, value);
;
                } catch (Exception e) {
                    logger.error("PropertyVo does not have getter method for property: {}", field, e);
                }
            }

            leanPropertyMapList.add(propertyMap);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", leanPropertyMapList);

        return resultMap;
        // 計算 pagination
    }
}
