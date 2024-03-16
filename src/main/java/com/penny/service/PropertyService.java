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

    @Autowired
    public PropertyService(PropertyVoMapper propertyVoMapper, Paginator paginator){
        this.propertyVoMapper = propertyVoMapper;
        this.paginator = paginator;
    }

    /**
     * 根據給定的搜尋請求，獲取房源列表並進行篩選。
     *
     * @param propertySearchRequest 搜尋請求物件，包含篩選條件、返回欄位、排序方式、分頁資訊等。
     * @return 包含房源列表和分頁資訊的結果 Map。
     */
    public Map<String, Object> getPropertiesByFilter(PropertySearchRequest propertySearchRequest) {

        // 確保所有的請求參數不為 null，避免空指針異常(NullPointerException)
        PropertySearchRequest processedPropertySearchRequest = PropertySearchRequest
                .builder()
                .filterMap(Optional.ofNullable(propertySearchRequest.getFilterMap()).orElse(new HashMap<>()))
                .returnFieldList(Optional.ofNullable(propertySearchRequest.getReturnFieldList()).orElse(new ArrayList<>()))
                .sortMap(Optional.ofNullable(propertySearchRequest.getSortMap()).orElse(new HashMap<>()))
                .page(Optional.ofNullable(propertySearchRequest.getPage()).orElse(DEFAULT_PAGE))
                .limit(Optional.ofNullable(propertySearchRequest.getLimit()).orElse(DEFAULT_LIMIT))
                .build();

        // 驗證參數合法性
        validateFilters(processedPropertySearchRequest);

        int page = processedPropertySearchRequest.getPage();
        int limit =processedPropertySearchRequest.getLimit();
        int offset = paginator.calculateOffset(page, limit);

        // 獲取房源列表
        List<PropertyVo> propertyVoList = fetchProperties(propertySearchRequest, offset, limit);

        // 轉換為精簡的房源Map列表
        List<Map<String, Object>> leanPropertyMapList = convertToLeanPropertyMapList(propertyVoList, processedPropertySearchRequest.getReturnFieldList());

        // 計算 pagination
        long totalResultCount = propertyVoMapper.countByFilter(processedPropertySearchRequest.getFilterMap());
        long totalPages = paginator.calculateTotalPages(totalResultCount, limit);
        Map<String, Object> pagination = paginator.buildPaginationMap(totalResultCount, page, totalPages, limit);

        // 建構結果 Map，包含房源列表和分頁資訊
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", leanPropertyMapList);
        resultMap.put("pagination", pagination);

        return resultMap;
    }

    /**
     * 驗證房源搜尋請求中的篩選條件。
     *
     * @param propertySearchRequest 房源搜尋請求物件，包含要進行驗證的篩選條件。
     * @throws FieldConflictException 當篩選條件存在衝突時拋出異常。
     */
    private void validateFilters(PropertySearchRequest propertySearchRequest){
        Map<String, Object> requestFilterMap = propertySearchRequest.getFilterMap();
        Integer numOfAvailableDays = (Integer) requestFilterMap.get("numOfAvailableDays");
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
            LocalDate startAvailableDate = LocalDate.parse(startAvailableDateString, formatter);
            LocalDate endAvailableDate = LocalDate.parse(endAvailableDateString, formatter);

            if (startAvailableDate.isAfter(endAvailableDate)) {
                throw new FieldConflictException("filter startAvailableDate cannot be later than endAvailableDate");
            }

            requestFilterMap.put("startAvailableDate", startAvailableDate);
            requestFilterMap.put("endAvailableDate", endAvailableDate);
        }

        // 檢查頁碼
        if (propertySearchRequest.getPage() != null && propertySearchRequest.getPage() <= 0) {
            throw new FieldConflictException("page cannot be less than or equals to 0");
        }
    }

    /**
     * 根據房源搜索請求，獲取房源列表。
     *
     * @param propertySearchRequest 房源搜索請求物件，包含要應用於查詢的篩選條件、返回欄位和排序方式。
     * @param offset                查詢結果的偏移量。
     * @param limit                 查詢結果的限制數量。
     * @return 包含房源列表的結果 Map。
     */
    private List<PropertyVo> fetchProperties(PropertySearchRequest propertySearchRequest, int offset, int limit) {
        Map<String, Object> requestFilterMap = propertySearchRequest.getFilterMap();
        List<String> requestReturnFieldList = propertySearchRequest.getReturnFieldList();
        Map<String, String> requestSortMap = propertySearchRequest.getSortMap();

        SelectPropertyParam selectPropertyParam = SelectPropertyParam
                .builder()
                .filterMap(requestFilterMap)
                .returnFieldList(requestReturnFieldList)
                .sortMap(requestSortMap)
                .offset(offset)
                .limit(limit)
                .build();

        return propertyVoMapper.listByFilter(selectPropertyParam);
    }

    /**
     * 將房源物件列表轉換為精簡的房源 Map 列表。
     *
     * @param propertyVoList       房源物件列表。
     * @param requestReturnFieldList 返回的房源屬性欄位列表。
     * @return 包含精簡的房源 Map 的列表。
     */
    private List<Map<String, Object>> convertToLeanPropertyMapList(List<PropertyVo> propertyVoList, List<String> requestReturnFieldList) {
        List<Map<String, Object>> leanPropertyMapList = new ArrayList<>();

        for (PropertyVo propertyVo : propertyVoList) {
            Map<String, Object> propertyMap = new HashMap<>();

            for (String field : requestReturnFieldList) {
                // 處理地址屬性
                if (field.equals("address")) {
                    propertyMap.put("apartmentFloor", propertyVo.getApartmentFloor());
                    propertyMap.put("street", propertyVo.getStreet());
                    propertyMap.put("districtId", propertyVo.getAdminAreaLevel3DistrictId());
                    continue;
                }

                // 處理地區屬性
                if (field.equals("district")) {
                    propertyMap.put("districtId", propertyVo.getDistrictId());
                    propertyMap.put("districtName", propertyVo.getDistrictName());
                    propertyMap.put("parentDistrictId", propertyVo.getParentDistrictId());
                    propertyMap.put("parentDistrictName", propertyVo.getParentDistrictName());
                    continue;
                }

                // 處理其他屬性
                String getterMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);

                try {
                    Method getterMethod = propertyVo.getClass().getMethod(getterMethodName);
                    Object value = getterMethod.invoke(propertyVo);
                    propertyMap.put(field, value);
                } catch (Exception e) {
                    logger.error("PropertyVo does not have getter method for property: {}", field, e);
                }
            }

            leanPropertyMapList.add(propertyMap);
        }

        return leanPropertyMapList;
    }
}
