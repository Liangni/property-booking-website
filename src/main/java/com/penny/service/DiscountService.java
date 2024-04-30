package com.penny.service;

import com.penny.dao.DiscountVoMapper;
import com.penny.dao.PropertyDiscountVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.PropertyDiscountBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.CreatePropertyDiscountRequest;
import com.penny.vo.DiscountVo;
import com.penny.vo.PropertyDiscountVo;
import com.penny.vo.base.PropertyBaseVo;
import com.penny.vo.base.PropertyDiscountBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountVoMapper discountVoMapper;

    private final PropertyDiscountBaseVoMapper propertyDiscountBaseVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final EcUserService ecUserService;

    private final PropertyDiscountVoMapper propertyDiscountVoMapper;

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

    /**
     * 根據建立房源折扣請求來新增房源折扣。
     *
     * @param createRequest 建立房源折扣的請求物件
     * @throws FieldConflictException 如果未提供必要的 propertyId 或 discountId 時拋出
     */
    public void createPropertyDiscount(CreatePropertyDiscountRequest createRequest){
        Long propertyId = createRequest.getPropertyId();
        Long discountId = createRequest.getDiscountId();

        // 檢查參數
        if (propertyId == null) throw new FieldConflictException("propertyId is required");

        if (discountId == null) throw new FieldConflictException("discountId is required");

        // 查詢是否已存在相同的房源折扣
        PropertyDiscountVo existingPropertyDiscountVo = propertyDiscountVoMapper.selectByPropertyIdAndDiscountId(propertyId, discountId);

        // 如果已存在相同的房源折扣，則不進行新增操作
        if (existingPropertyDiscountVo != null) return;

        // 建立新的房源折扣資料
        PropertyDiscountBaseVo newPropertyDiscountBaseVo = PropertyDiscountBaseVo
                .builder()
                .discountId(discountId)
                .propertyId(propertyId)
                .build();

        // 儲存新的房源折扣資料
        propertyDiscountBaseVoMapper.insertSelective(newPropertyDiscountBaseVo);
    }
}
