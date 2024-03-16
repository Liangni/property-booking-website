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
        Integer numOfAvailableDays = null;
        LocalDate startAvailableDate = null;
        LocalDate endAvailableDate = null;

        Map<String, Object> requestFilterMap = propertySearchRequest.getFilterMap();
        if (requestFilterMap != null) {
            numOfAvailableDays = (Integer) requestFilterMap.get("numOfAvailableDays");
            String startAvailableDateString = (String) requestFilterMap.get("startAvailableDate");
            String endAvailableDateString = (String) requestFilterMap.get("endAvailableDate");

            // 檢查是否 numOfAvailableDays 和 startAvailableDate 同時存在
            boolean isNumOfAvailableDaysAndStartAvailableDatePresent =
                    numOfAvailableDays != null && startAvailableDateString != null;

            // 檢查是否 numOfAvailableDays 和 endAvailableDate 同時存在
            boolean isNumOfAvailableDaysAndEndAvailableDatePresent =
                    numOfAvailableDays != null && endAvailableDateString != null;

            // 如果有其中一種情況存在，則拋出異常
            if (isNumOfAvailableDaysAndStartAvailableDatePresent || isNumOfAvailableDaysAndEndAvailableDatePresent) {
                throw new FieldConflictException("filter numOfAvailableDays cannot co-exist with startAvailableDate or endAvailableDate");
            }

            // 確認 filterMap 中的 startAvailableDate 和 endAvailableDate 成對出現
            // 檢查是否只有其中一個存在且值不為 null
            boolean isOnlyOneDatePresentWithValueNotNull =
                    (startAvailableDateString != null && endAvailableDateString == null) ||
                            (startAvailableDateString == null && endAvailableDateString != null);

            if (isOnlyOneDatePresentWithValueNotNull) {
                throw new FieldConflictException("filter startAvailableDate and endAvailableDate should exist together");
            }

            // 若 filterMap 中的 startAvailableDate 和 endAvailableDate 同時存在，確認 startAvailableDate 不晚於 endAvailableDate
            boolean isStartAndEndAvailableDatePresent = startAvailableDateString != null && endAvailableDateString != null;
            if (isStartAndEndAvailableDatePresent) {
                // 定義日期格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 解析日期字符串為 LocalDate
                startAvailableDate = LocalDate.parse(startAvailableDateString, formatter);
                endAvailableDate = LocalDate.parse(endAvailableDateString, formatter);

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
            for (String field : requestReturnFieldList) {
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
        for (PropertyVo propertyVo : propertyVoList) {
            Map<String, Object> propertyMap = new HashMap<>();

            for (String field : requestReturnFieldList) {
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

        // 計算 pagination
        long totalResultCount = propertyVoMapper.countByFilter(requestFilterMap);
        long totalPages = paginator.calculateTotalPages(totalResultCount, limit);
        Map<String, Object> pagination = paginator.buildPaginationMap(totalResultCount, page, totalPages, limit);

        // 構建結果 Map，包含房源列表和分頁資訊
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", leanPropertyMapList);
        resultMap.put("pagination", pagination);

        return resultMap;
    }
}
