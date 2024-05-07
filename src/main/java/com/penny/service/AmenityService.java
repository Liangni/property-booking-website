package com.penny.service;

import com.penny.dao.AmenityTypeVoMapper;
import com.penny.dao.AmenityVoMapper;
import com.penny.dao.PropertyAmenityVoMapper;
import com.penny.dao.base.AmenityBaseVoMapper;
import com.penny.dao.base.PropertyAmenityBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.AmenityTypeVo;
import com.penny.vo.AmenityVo;
import com.penny.vo.PropertyAmenityVo;
import com.penny.vo.base.AmenityBaseVo;
import com.penny.vo.base.PropertyAmenityBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AmenityService {
    private final AmenityBaseVoMapper amenityBaseVoMapper;

     private final AmenityVoMapper amenityVoMapper;

     private final PropertyBaseVoMapper propertyBaseVoMapper;

     private final PropertyAmenityBaseVoMapper propertyAmenityBaseVoMapper;

     private final PropertyAmenityVoMapper propertyAmenityVoMapper;

     private final AmenityTypeVoMapper amenityTypeVoMapper;

    private final EcUserService ecUserService;

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
     * 創建房源設施。
     *
     * @param propertyId 房源 ID
     * @param amenityId 設施 ID
     * @throws ResourceNotFoundException 如果指定的房源或設施不存在，則拋出此異常。
     */
    @Transactional
     public void createPropertyAmenity(Long propertyId, Long amenityId) {
         // 檢查房源是否存在
         PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
         if(propertyBaseVo == null) {
             throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));
         }

         // 檢查設施是否存在
        AmenityBaseVo  amenityBaseVo = amenityBaseVoMapper.selectByPrimaryKey(amenityId);
        if( amenityBaseVo == null) {
            throw new ResourceNotFoundException("amenity with id %s not found".formatted(amenityId));
        }

         // 檢驗登入使用者是否為房源出租人
         ecUserService.validatePropertyOwnership(propertyBaseVo.getHostId());

         PropertyAmenityVo existingPropertyAmenityVo = propertyAmenityVoMapper.selectByPropertyIdAndAmenityId(propertyId, amenityId);

         if (existingPropertyAmenityVo != null) return;

         propertyAmenityBaseVoMapper.insertSelective(new PropertyAmenityBaseVo(null, propertyId, amenityId));
     }

    /**
     * 根據房源ID獲取房源設施信息。
     *
     * @param propertyId 要查詢的房源ID
     * @return 包含設施分類詳細資訊的 map 列表
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發布，則拋出此異常
     */
    public List<Map<String, Object>>  listPublishedPropertyAmenityMap(Long propertyId) {
        // 檢查房源是否存在及已發佈
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));
        }

        // 根據房源ID查詢房源設施列表
        List<AmenityVo> propertyAmenityList = amenityVoMapper.listByPropertyId(propertyId);

        // 將設施列表按類型分類並返回
        return classifyAmenitiesByType(propertyAmenityList);
    }

    /**
     * 返回指定房源的設施列表，並按類型分類。
     *
     * @param propertyId 房源的 ID
     * @return 包含設施分類詳細資訊的地圖列表
     * @throws ResourceNotFoundException 如果找不到指定房源時拋出
     */
    public List<Map<String, Object>>  listPropertyAmenityMap(Long propertyId) {
        // 檢查房源是否存在
        PropertyBaseVo propertyBaseVo = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property with id %s not found".formatted(propertyId)));

        // 檢驗登入使用者是否為房源出租人
        ecUserService.validatePropertyOwnership(propertyBaseVo.getHostId());

        // 根據房源ID查詢房源設施列表
        List<AmenityVo> propertyAmenityList = amenityVoMapper.listByPropertyId(propertyId);

        // 將設施列表按類型分類並返回
        return classifyAmenitiesByType(propertyAmenityList);
    }

    /**
     * 根據刪除請求來刪除房源設施。
     *
     * @param propertyId 房源 ID
     * @param amenityId 設施 ID
     * @throws ResourceNotFoundException 如果找不到目標房源設施時拋出
     */
    @Transactional
    public void deletePropertyAmenity(Long propertyId, Long amenityId) {
        int deleteCount = propertyAmenityVoMapper.deleteByPropertyIdAndAmenityId(propertyId, amenityId);
        if (deleteCount == 0) {
            throw new ResourceNotFoundException("property amenity with property id %s and amenity id %s not found".formatted(propertyId, amenityId));
        }
    }

    /**
     * 將設施分類至其對應的設施類型中。
     *
     * @param amenityList 要分類的設施列表
     * @return 包含設施類型詳細資訊的列表
     */
    private List<Map<String, Object>> classifyAmenitiesByType(List<AmenityVo> amenityList) {
        // 取得所有設施類型
        List<AmenityTypeVo> allAmenityTypes = amenityTypeVoMapper.listAll();

        // 儲存設施類型詳細資訊的 map
        Map<Long, Map<String, Object>> amenityTypeMap = new HashMap<>();
        allAmenityTypes.forEach(amenityType -> {
            // 設施類型的詳細資訊
            Map<String, Object> amenityTypeDetails = new HashMap<>();
            amenityTypeDetails.put("amenityTypeId", amenityType.getAmenityTypeId());
            amenityTypeDetails.put("amenityTypeName", amenityType.getAmenityTypeName());
            amenityTypeDetails.put("amenities", new ArrayList<AmenityVo>());
            amenityTypeMap.put(amenityType.getAmenityTypeId(), amenityTypeDetails);
        });

        // 遍歷所有設施，將每個設施添加到其所屬的設施類型中
        amenityList.forEach(amenity -> {
            Long amenityTypeId = amenity.getAmenityTypeId();
            if (amenityTypeMap.containsKey(amenityTypeId)) {
                // 從設施類型 map 中取得相應的設施類型的設施列表
                List<AmenityVo> amenitiesOfType = (List<AmenityVo>) amenityTypeMap.get(amenityTypeId).get("amenities");
                // 將當前設施添加到設施類型的設施列表中
                amenitiesOfType.add(amenity);
            }
        });

        // 設施類型詳細資訊的列表
        List<Map<String, Object>> amenityTypeDetailsList = new ArrayList<>(amenityTypeMap.values());

        return amenityTypeDetailsList;
    }
}
