package com.penny.service;

import com.penny.dao.PropertyVoMapper;
import com.penny.request.property.PropertySearchRequest;
import com.penny.vo.PropertyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InitialPageService {
    private static final int DEFAULT_NUM_OF_AVAILABLE_DAYS = 5;
    private static final Integer DEFAULT_PICTURE_DT_SIZE = 1;

    private final PropertyVoMapper propertyVoMapper;
    private final PropertyService propertyService;
    private final PictureService pictureService;

    /**
     * 獲取初始頁面資料，包括房源圖片和相關屬性。
     *
     * @return 包含初始頁面資料的 Map
     */
    public Map<String, Object> getInitialPageData(){
        // 準備房源查詢參數
        PropertySearchRequest request = PropertySearchRequest
                .builder()
                .numOfAvailableDays(DEFAULT_NUM_OF_AVAILABLE_DAYS)
                .build();

        // 根據房源查詢參數查詢房源列表
        List<PropertyVo> propertyVoList = propertyVoMapper.listByNumOfAvailableDays(request);

        // 準備用於存儲房源資訊的列表
        List<Map<String, Object>> propertyMapList = new ArrayList<>();
        for(PropertyVo propertyVo: propertyVoList) {
            // 只保留已發布的房源資訊
            if (!propertyVo.getIsPublished()) continue;

            // 查詢房源的 DEFAULT_SIZE 的圖片DT列表，並取得下載圖片的預簽名 url
            List<Map<String, Object>> propertyImageUrlList= pictureService.listPropertyImageDownloadUrls(propertyVo.getPropertyId(), DEFAULT_PICTURE_DT_SIZE);

            // 將房源資訊加入列表
            propertyMapList.add(preparePropertyMap(propertyVo, propertyImageUrlList));
        }

        // 準備用於存儲結果的 Map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("propertyList", propertyMapList);

        return resultMap;
    }

    /**
     * 準備房源資訊的 Map。
     *
     * @param propertyVo          房源物件
     * @param propertyImageUrlList 房源圖片的 URL 列表
     * @return 房源資訊的 Map
     */
    private Map<String, Object> preparePropertyMap(PropertyVo propertyVo, List<Map<String, Object>> propertyImageUrlList) {
        return Map.ofEntries(
                Map.entry("imageUrls", propertyImageUrlList),
                Map.entry("districtId", propertyVo.getDistrictId()),
                Map.entry("districtName", propertyVo.getDistrictName()),
                Map.entry("parentDistrictId", propertyVo.getParentDistrictId()),
                Map.entry("parentDistrictName", propertyVo.getParentDistrictName()),
                Map.entry("propertyId", propertyVo.getPropertyId()),
                Map.entry("propertyTitle", propertyVo.getPropertyTitle()),
                Map.entry("priceOnWeekends", propertyVo.getPriceOnWeekends()),
                Map.entry("priceOnWeekdays", propertyVo.getPriceOnWeekdays()),
                Map.entry("averageRating", propertyVo.getAverageRating()),
                Map.entry("reviewCount", propertyVo.getReviewCount())
        );
    }

}
