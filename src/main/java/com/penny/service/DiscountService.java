package com.penny.service;

import com.penny.dao.DiscountVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.DiscountVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountVoMapper discountVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final EcUserService ecUserService;

    /**
     * 獲取特定已公開房源的折扣列表。
     *
     * @param propertyId 房源ID
     * @throws FieldConflictException 如果 propertyId 為 null，則拋出此異常
     * @throws ResourceNotFoundException 如果找不到指定的已發佈房源，則拋出此異常
     * @return 房源折扣列表
     */
    public List<DiscountVo> getPublishedPropertyDiscount(Long propertyId) {
        // 檢查參數
        if (propertyId == null) throw new FieldConflictException("propertyId is required");

        // 檢查房源是否存在及已發佈
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with propertyId %s is not found".formatted(propertyId));
        }

        // 根據房源ID查詢相應的折扣列表，並過濾出已啟用的折扣後返回
        return discountVoMapper.listByPropertyId(propertyId);
    }

    /**
     * 獲取特定房源的折扣列表。
     *
     * @param propertyId 房源ID
     * @throws FieldConflictException 如果 propertyId 為 null，則拋出此異常
     * @throws ResourceNotFoundException 如果找不到指定的已發佈房源，則拋出此異常
     * @return 房源折扣列表
     */
    public List<DiscountVo> getPropertyDiscount(Long propertyId) {
        // 檢查參數
        if (propertyId == null) throw new FieldConflictException("propertyId is required");

        // 檢查房源是否存在
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null) {
            throw new ResourceNotFoundException("property with propertyId %s is not found".formatted(propertyId));
        }

        // 檢驗登入使用者是否為房源出租人
        ecUserService.validatePropertyOwnership(propertyBaseVo.getHostId());

        // 根據房源ID查詢相應的折扣列表，並過濾出已啟用的折扣後返回
        return discountVoMapper.listByPropertyId(propertyId);
    }
}
