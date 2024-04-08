package com.penny.service;

import com.penny.dao.PropertyVoMapper;
import com.penny.request.property.PropertySearchParam;
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

    /**
     * 獲取初始頁面資料，包括房源圖片和相關屬性。
     *
     * @return 包含初始頁面資料的 Map
     */
    public Map<String, Object> getInitialPageData(){
        // 準備房源查詢參數
        PropertySearchParam request = PropertySearchParam
                .builder()
                .numOfAvailableDays(DEFAULT_NUM_OF_AVAILABLE_DAYS)
                .build();

        // 根據房源查詢參數查詢房源列表
        List<PropertyVo> propertyVoList = propertyVoMapper.listByNumOfAvailableDays(request);

        // 準備用於存儲房源資訊的列表
        List<Map<String, Object>> propertyMapList = new ArrayList<>();
        for(PropertyVo propertyVo: propertyVoList) {
            // 查詢房源的 DEFAULT_SIZE 的圖片DT列表，並取得下載圖片的預簽名 url
            List<String> propertyImageUrlList= propertyService.getImageDownloadUrl(propertyVo.getPropertyId(), DEFAULT_PICTURE_DT_SIZE);
            // 準備房源資訊的 Map
            Map<String, Object> propertyMap = preparePropertyMap(propertyVo, propertyImageUrlList);
            // 將房源資訊加入列表
            propertyMapList.add(propertyMap);
        }

        // 準備用於存儲結果的 Map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("propertyList", propertyMapList);

        return resultMap;
    }

    /**
     * 準備房源資訊的 Map。
     *
     * @param propertyVo          房源對象
     * @param propertyImageUrlList 房源圖片的 URL 列表
     * @return 房源資訊的 Map
     */
    private Map<String, Object> preparePropertyMap(PropertyVo propertyVo, List<String> propertyImageUrlList) {
        return new MapBuilder()
                .put("imageUrls", propertyImageUrlList)
                .put("districtId", propertyVo.getDistrictId())
                .put("districtName", propertyVo.getDistrictName())
                .put("parentDistrictId", propertyVo.getParentDistrictId())
                .put("parentDistrictName", propertyVo.getParentDistrictName())
                .put("propertyId", propertyVo.getPropertyId())
                .put("propertyTitle", propertyVo.getPropertyTitle())
                .put("priceOnWeekends", propertyVo.getPriceOnWeekends())
                .put("priceOnWeekdays", propertyVo.getPriceOnWeekdays())
                .put("averageRating", propertyVo.getAverageRating())
                .put("reviewCount", propertyVo.getReviewCount())
                .build();
    }

    /**
     * 用於動態構建 Map 的內部類。
     */
    private static class MapBuilder {
        private final Map<String, Object> map;

        /**
         * 創建一個新的 MapBuilder 實例。
         */
        public MapBuilder() {
            this.map = new HashMap<>();
        }

        /**
         * 在 Map 中添加鍵值對。
         *
         * @param key   鍵
         * @param value 值
         * @return 返回當前 MapBuilder 實例
         */
        public MapBuilder put(String key, Object value) {
            map.put(key, value);
            return this;
        }

        /**
         * 構建並返回最終的 Map 物件。
         *
         * @return 構建完成的 Map 物件
         */
        public Map<String, Object> build() {
            return map;
        }
    }

}
