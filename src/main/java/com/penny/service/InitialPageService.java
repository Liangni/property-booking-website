package com.penny.service;

import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyReviewVoMapper;
import com.penny.dao.PropertyVoMapper;
import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.util.Paginator;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class InitialPageService {
    private static final int DEFAULT_PROPERTY_PAGE = 1;

    private static final int DEFAULT_PROPERTY_LIMIT = 10;

    private static final int DEFAULT_NUM_OF_AVAILABLE_DAYS = 5;

    private static final int DEFAULT_PICTURE_DT_PAGE = 1;

    private static final int DEFAULT_PICTURE_DT_LIMIT = 10;

    private static final Integer DEFAULT_PICTURE_DT_SIZE = 1;

    private final PropertyVoMapper propertyVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final PropertyReviewVoMapper propertyReviewVoMapper;

    private final Paginator paginator;

    @Autowired
    public InitialPageService(PropertyVoMapper propertyVoMapper, PictureDtVoMapper pictureDtVoMapper, PropertyReviewVoMapper propertyReviewVoMapper, Paginator paginator){
        this.propertyVoMapper = propertyVoMapper;
        this.pictureDtVoMapper = pictureDtVoMapper;
        this.propertyReviewVoMapper = propertyReviewVoMapper;
        this.paginator = paginator;
    }
    /**
     * 獲取初始頁面數據。
     * 此方法構建並返回用於初始頁面的數據，包括房源列表、分頁信息等。
     *
     * @return 返回包含初始頁面數據的 Map。其中，鍵為 "result"，值為房源列表；鍵為 "pagination"，值為分頁信息。
     */
    public Map<String, Object> getInitialPageData(){
        // 準備過濾屬性 map
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("numOfAvailableDays", DEFAULT_NUM_OF_AVAILABLE_DAYS);

        // 準備返回屬性列表
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("propertyId");
        returnFieldList.add("district");
        returnFieldList.add("priceOnWeekdays");

        // 準備排序 map
        Map<String, String> sortMap = new HashMap<>();
        sortMap.put("district", "asc");

        // 計算房源的偏移量
        int propertyOffset = paginator.calculateOffset(DEFAULT_PROPERTY_PAGE, DEFAULT_PROPERTY_LIMIT);
        // 準備房源查詢參數
        SelectPropertyParam selectPropertyParam = SelectPropertyParam
                .builder()
                .filterMap(filterMap)
                .returnFieldList(returnFieldList)
                .sortMap(sortMap)
                .offset(propertyOffset)
                .limit(DEFAULT_PROPERTY_LIMIT)
                .build();

        // 根據房源查詢參數查詢房源列表
        List<PropertyVo> propertyVoList = propertyVoMapper.listByFilter(selectPropertyParam);

        // 計算圖片詳細資訊的偏移量並為每個房源設置圖片詳細資訊列表和評論數
        int pictureDtOffset = paginator.calculateOffset(DEFAULT_PICTURE_DT_PAGE, DEFAULT_PICTURE_DT_LIMIT);
        List<Map<String, Object>> leanPropertyList= new ArrayList<>();

        for(PropertyVo propertyVo: propertyVoList) {
            Long propertyId = propertyVo.getPropertyId();
            Long priceOnWeekdays = propertyVo.getPriceOnWeekdays();
            String districtName = propertyVo.getDistrictName();
            String parentDistrictName = propertyVo.getParentDistrictName();
            LocalDate startAvailableDate = propertyVo.getStartAvailableDate();
            LocalDate endAvailableDate = propertyVo.getEndAvailableDate();
            Double averageRating = propertyVo.getAverageRating();

            // 創建一個新的房源 Map，存放房源的資訊
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("propertyId", propertyId);
            propertyMap.put("priceOnWeekdays", priceOnWeekdays);
            propertyMap.put("parentDistrictName", parentDistrictName);
            propertyMap.put("districtName", districtName);
            propertyMap.put("startAvailableDate", startAvailableDate);
            propertyMap.put("endAvailableDate", endAvailableDate);
            propertyMap.put("averageRating", averageRating);

            // 查詢房源的圖片詳細資訊列表
            List<PictureDtVo> pictureDtVoList = pictureDtVoMapper.listByPropertyIdAndSize(propertyId, DEFAULT_PICTURE_DT_SIZE, pictureDtOffset, DEFAULT_PICTURE_DT_LIMIT);
            propertyMap.put("pictureDtList", pictureDtVoList);

            // 查詢房源的評論數
            Long reviewCount = propertyReviewVoMapper.countByPropertyId(propertyId);
            propertyMap.put("reviewCount", reviewCount);

            // 將房源 Map 添加到 leanPropertyList 中
            leanPropertyList.add(propertyMap);
        }

        // 獲取總房源數和總頁數，並構建分頁資訊 Map
        long totalResultCount = propertyVoMapper.countByFilter(filterMap);
        long totalPages = paginator.calculateTotalPages(totalResultCount, DEFAULT_PROPERTY_LIMIT);
        Map<String, Object> pagination = paginator.buildPaginationMap(totalResultCount, DEFAULT_PROPERTY_PAGE, totalPages, DEFAULT_PROPERTY_LIMIT);

        // 構建結果 Map，包含房源列表和分頁資訊
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", leanPropertyList);
        resultMap.put("pagination", pagination);

        return resultMap;
    }

}
