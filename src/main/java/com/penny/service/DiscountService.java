package com.penny.service;

import com.penny.dao.DiscountVoMapper;
import com.penny.dao.PropertyDiscountVoMapper;
import com.penny.dao.base.DiscountBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.PropertyDiscountBaseVoMapper;
import com.penny.exception.ResourceDuplicateException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.CreatePropertyDiscountRequest;
import com.penny.vo.DiscountVo;
import com.penny.vo.PropertyDiscountVo;
import com.penny.vo.base.DiscountBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import com.penny.vo.base.PropertyDiscountBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountBaseVoMapper discountBaseVoMapper;

    private final DiscountVoMapper discountVoMapper;

    private final PropertyDiscountBaseVoMapper propertyDiscountBaseVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final EcUserService ecUserService;

    private final PropertyDiscountVoMapper propertyDiscountVoMapper;

    /**
     * 獲取特定已公開房源的折扣列表。
     *
     * @param propertyId 房源ID
     * @throws ResourceNotFoundException 如果找不到指定的已發佈房源，則拋出此異常
     * @return 房源折扣列表
     */
    public List<DiscountVo> getPublishedPropertyDiscount(Long propertyId) {

        // 檢查房源是否存在及已發佈
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));
        }

        // 根據房源ID查詢相應的折扣列表，並過濾出已啟用的折扣後返回
        return discountVoMapper.listByPropertyId(propertyId);
    }

    /**
     * 獲取特定房源的折扣列表。
     *
     * @param propertyId 房源ID
     * @throws ResourceNotFoundException 如果找不到指定的已發佈房源，則拋出此異常
     * @return 房源折扣列表
     */
    public List<DiscountVo> getPropertyDiscount(Long propertyId) {
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
     * 根據房源 ID 建立新的房源折扣資料。
     *
     * @param propertyId     房源 ID
     * @param createRequest  建立房源折扣的請求
     * @throws ResourceDuplicateException    如果該房源已存在相同折扣或該房源已存在相同最少預定天數的折扣，則拋出資源重複異常
     */
    public void createPropertyDiscount(Long propertyId, CreatePropertyDiscountRequest createRequest){
        // 取得建立房源折扣請求中的折扣值和最少預訂天數
        Double discountValue = createRequest.getDiscountValue();
        Integer leastNumOfBookingDays = createRequest.getLeastNumOfBookingDays();
        Long discountId;

        // 根據折扣值和最少預訂天數查詢是否已存在相同折扣
        DiscountVo existingDiscountVo = discountVoMapper.selectByDiscountValueAndLeastNumOfBookingDays(discountValue, leastNumOfBookingDays);

        // 如果已存在相同折扣，則取得該折扣的折扣 ID
        if (existingDiscountVo != null) {
            discountId = existingDiscountVo.getDiscountId();
        } else {
            // 否則新增一個新的折扣
            DiscountBaseVo newDiscountBaseVo = DiscountBaseVo
                    .builder()
                    .discountValue(discountValue)
                    .leastNumOfBookingDays(leastNumOfBookingDays)
                    .build();

            discountBaseVoMapper.insertSelective(newDiscountBaseVo);
            discountId = newDiscountBaseVo.getDiscountId();
        }

        // 查詢是否已存在相同的房源折扣
        List<DiscountVo> propertyDiscountList = discountVoMapper.listByPropertyId(propertyId);

        for (DiscountVo discountVo: propertyDiscountList) {
            // 如果已存在相同的房源折扣，則拋出錯誤
            if (discountVo.getDiscountId().equals(discountId)) {
                throw new ResourceDuplicateException("Property discount with discountId %s already exists".formatted(discountId));
            }

            // 如果已存在有相同最少預定天數的房源折扣，則拋出錯誤
            if (discountVo.getLeastNumOfBookingDays().equals(leastNumOfBookingDays)) {
                throw new ResourceDuplicateException("Property already has discount with the same leastNumOfBookingDays");
            }
        }

        // 建立新的房源折扣資料
        PropertyDiscountBaseVo newPropertyDiscountBaseVo = PropertyDiscountBaseVo
                .builder()
                .discountId(discountId)
                .propertyId(propertyId)
                .build();

        // 儲存新的房源折扣資料
        propertyDiscountBaseVoMapper.insertSelective(newPropertyDiscountBaseVo);
    }

    /**
     * 根據刪除請求來刪除房源設施。
     *
     * @param propertyId 房源 ID
     * @param discountId 折扣 ID
     * @throws ResourceNotFoundException 如果找不到目標房源折扣時拋出
     */
    @Transactional
    public void deletePropertyDiscount(Long propertyId, Long discountId) {
        int deleteCount = propertyDiscountVoMapper.deleteByPropertyIdAndDiscountId(propertyId, discountId);
        if (deleteCount == 0) {
            throw new ResourceNotFoundException("property discount with property id %s discount id %s not found".formatted(propertyId, discountId));
        }
    }
}
