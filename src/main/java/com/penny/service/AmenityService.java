package com.penny.service;

import com.penny.dao.AmenityVoMapper;
import com.penny.dao.base.AmenityBaseVoMapper;
import com.penny.dao.base.AmenityTypeBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.AmenityVo;
import com.penny.vo.base.AmenityBaseVo;
import com.penny.vo.base.AmenityTypeBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AmenityService {
     private final AmenityVoMapper amenityVoMapper;

     private final AmenityTypeBaseVoMapper amenityTypeBaseVoMapper;

     private final PropertyBaseVoMapper propertyBaseVoMapper;


    /**
     * 取得設施列表。
     *
     * @param amenityTypeId 設施類型 ID
     * @return 設施列表
     */
     public List<AmenityVo> getAmenities(Long amenityTypeId) {

         return amenityVoMapper.listByAmenityTypeId(amenityTypeId);
     }

    /**
     * 根據房源ID獲取房源設施信息。
     *
     * @param propertyId 要查詢的房源ID。
     * @return 返回一個 map，其中包含根據設施類型分類的設施列表。
     * @throws FieldConflictException 如果房源ID為空，則拋出 FieldConflictException 異常。
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發布，則拋出此異常
     */
    public Map<String, List<AmenityVo>> getPublishedPropertyAmenityMap(Long propertyId) {
        // 檢查參數，如果房源ID為空，拋出異常
        if(propertyId == null) {
            throw new FieldConflictException("propertyId is required");
        }

        // 檢查房源是否存在及已發佈
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with propertyId %s is not found".formatted(propertyId));
        }

        // 根據房源ID查詢房源設施列表
        List<AmenityVo> propertyAmenityList = amenityVoMapper.listByPropertyId(propertyId);

        // 將設施列表按類型分類並返回
        return groupByAmenityType(propertyAmenityList);
    }

    /**
     * 根據設施類型將設施列表進行分類。
     *
     * @param amenityList 要分類的設施列表。
     * @return 返回一個 map，其中包含根據設施類型分類的設施列表。
     */
    private Map<String, List<AmenityVo>> groupByAmenityType(List<AmenityVo> amenityList) {
        Map<String, List<AmenityVo>> amenityTypeNameMap = new HashMap<>();
        Map<Long, String> amenityIdNameMap = new HashMap<>();

        // 遍歷設施列表，將設施按類型進行分類
       amenityList
                .forEach(amenity -> {
                    Long amenityTypeId = amenity.getAmenityTypeId();
                    String amenityTypeName = amenityIdNameMap.get(amenityTypeId);

                    // 如果設施類型已存在於結果 map 中，將設施添加到相應的類型列表中
                    if(amenityTypeName != null) {
                        amenityTypeNameMap.get(amenityTypeName).add(amenity);
                    } else {
                        // 否則從資料庫中獲取設施類型名稱，並將設施添加到新的類型列表中
                        AmenityTypeBaseVo amenityTypeBaseVo = amenityTypeBaseVoMapper.selectByPrimaryKey(amenityTypeId);
                        amenityTypeName = amenityTypeBaseVo.getAmenityTypeName();

                        // 初始化新的類型列表並添加設施
                        amenityTypeNameMap.put(amenityTypeName, new ArrayList<>());
                        amenityTypeNameMap.get(amenityTypeName).add(amenity);

                        // 將設施類型ID與類型名稱 map 存起來，避免重複查詢數據庫
                        amenityIdNameMap.put(amenityTypeId, amenityTypeName);
                    }
                });

       return amenityTypeNameMap;
    }
}
