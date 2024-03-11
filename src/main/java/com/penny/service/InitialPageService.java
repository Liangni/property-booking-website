package com.penny.service;

import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyReviewVoMapper;
import com.penny.dao.PropertyVoMapper;
import com.penny.daoParam.propertyVoMapper.ListByNumOfAvailableDaysParam;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InitialPageService {
    private static final int DEFAULT_PROPERTY_PAGE = 1;

    private static final int DEFAULT_PROPERTY_LIMIT = 10;

    private static final int DEFAULT_NUM_OF_AVAILABLE_DAY = 5;

    private static final int DEFAULT_PICTURE_DT_PAGE = 1;

    private static final int DEFAULT_PICTURE_DT_LIMIT = 10;

    private static final String DEFAULT_PICTURE_DT_SIZE = "small";

    private final PropertyVoMapper propertyVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final PropertyReviewVoMapper propertyReviewVoMapper;

    @Autowired
    public InitialPageService(PropertyVoMapper propertyVoMapper, PictureDtVoMapper pictureDtVoMapper, PropertyReviewVoMapper propertyReviewVoMapper){
        this.propertyVoMapper = propertyVoMapper;
        this.pictureDtVoMapper = pictureDtVoMapper;
        this.propertyReviewVoMapper = propertyReviewVoMapper;
    }

    public Map<String, Object> getInitialPageData(){
        // 準備返回屬性列表
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("propertyId");
        returnFieldList.add("district");
        returnFieldList.add("priceOnWeekdays");

        // 準備排序屬性列表和排序方式列表
        List<String> sortFieldList = List.of("district", "nearestAvailableDay");
        List<String> sortOrderList = List.of("asc", "asc");

        // 計算房源的偏移量
        int propertyOffset = calculateOffset(DEFAULT_PROPERTY_PAGE, DEFAULT_PROPERTY_LIMIT);
        // 準備房源查詢參數
        ListByNumOfAvailableDaysParam param  = ListByNumOfAvailableDaysParam
                .builder()
                .numOfAvailableDay(DEFAULT_NUM_OF_AVAILABLE_DAY)
                .returnFieldList(returnFieldList)
                .sortFieldList(sortFieldList)
                .sortOrderList(sortOrderList)
                .offset(propertyOffset)
                .limit(DEFAULT_PROPERTY_LIMIT)
                .build();

        // 根據房源查詢參數查詢房源列表
        List<PropertyVo> propertyVoList = propertyVoMapper.listByNumOfAvailableDays(param);


        // 計算圖片詳細資訊的偏移量並為每個房源設置圖片詳細資訊列表和評論數
        int pictureDtOffset = calculateOffset(DEFAULT_PICTURE_DT_PAGE, DEFAULT_PICTURE_DT_LIMIT);
        List<Map<String, Object>> leanProeprtyList= new ArrayList<>();

        for(PropertyVo propertyVo: propertyVoList) {
            Long propertyId = propertyVo.getPropertyId();
            Long priceOnWeekdays = propertyVo.getPriceOnWeekdays();
            String adminAreaLevel1DistrictName = propertyVo.getAdminAreaLevel1DistrictName();
            String adminAreaLevel2DistrictName = propertyVo.getAdminAreaLevel2DistrictName();
            String adminAreaLevel3DistrictName = propertyVo.getAdminAreaLevel3DistrictName();
            Date startAvailableDate = propertyVo.getStartAvailableDate();
            Date endAvailableDate = propertyVo.getEndAvailableDate();
            Double averageRating = propertyVo.getAverageRating();

            // 創建一個新的房源 Map，存放房源的資訊
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("propertyId", propertyId);
            propertyMap.put("priceOnWeekdays", priceOnWeekdays);
            propertyMap.put("parentDistrictName", Optional.ofNullable(adminAreaLevel1DistrictName)
                    .orElse(adminAreaLevel2DistrictName)
            );
            propertyMap.put("childDistrictName", adminAreaLevel3DistrictName);
            propertyMap.put("startAvailableDate", startAvailableDate);
            propertyMap.put("endAvailableDate", endAvailableDate);
            propertyMap.put("averageRating", averageRating);

            // 查詢房源的圖片詳細資訊列表
            List<PictureDtVo> pictureDtVoList = pictureDtVoMapper.listByPropertyId(propertyId, DEFAULT_PICTURE_DT_SIZE, pictureDtOffset, DEFAULT_PICTURE_DT_LIMIT);
            propertyMap.put("pictureDtList", pictureDtVoList);

            // 查詢房源的評論數
            Long reviewCount = propertyReviewVoMapper.countByPropertyId(propertyId);
            propertyMap.put("reviewCount", reviewCount);

            // 將房源 Map 添加到 leanPropertyList 中
            leanProeprtyList.add(propertyMap);
        }

        long totalResultCount = propertyVoMapper.countByNumOfAvailableDays(DEFAULT_NUM_OF_AVAILABLE_DAY);
        long totalPages = calculateTotalPages(totalResultCount, DEFAULT_PROPERTY_LIMIT);

        Map<String, Object> pagination = buildPaginationMap(totalResultCount, DEFAULT_PROPERTY_PAGE, totalPages, DEFAULT_PROPERTY_LIMIT);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("results", leanProeprtyList);
        resultMap.put("pagination", pagination);
        return resultMap;
    }

    /**
     * 根據頁碼和每頁數量計算分頁查詢時的偏移量。
     *
     * @param page  目標頁碼。
     * @param limit 每頁數量。
     * @return 分頁查詢時的偏移量。
     */
    private int calculateOffset(int page, int limit) {
        return (page - 1) * limit;
    }

    /**
     * 根據總記錄數和每頁數量計算總頁數。
     *
     * @param totalResultCount 總記錄數。
     * @param limit            每頁數量。
     * @return 總頁數。
     */
    private int calculateTotalPages(int totalResultCount, int limit) {
        return (int) Math.ceil((double) totalResultCount / limit);
    }

    private int calculateTotalPages(long totalResultCount, long limit) {
        return (int) Math.ceil((double) totalResultCount / limit);
    }

    /**
     * 構建分頁資訊的 map。
     *
     * @param totalResultCount 總記錄數。
     * @param page             目前頁碼。
     * @param totalPages       總頁數。
     * @param limit            每頁數量。
     * @return 紀錄分頁資訊的map。
     */
    private Map<String, Object> buildPaginationMap(long totalResultCount, long page, long totalPages, long limit) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalResults", totalResultCount);
        pagination.put("currentPage", page);
        pagination.put("totalPages", totalPages);
        pagination.put("pageSize", limit);
        return pagination;
    }
}
