package com.penny.service;

import com.penny.dao.PropertyReviewVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.PropertyReviewVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyReviewService {
    private final PropertyReviewVoMapper propertyReviewVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    /**
     * 獲取特定房源的評論列表。
     *
     * @param propertyId 房源ID
     * @throws ResourceNotFoundException 如果找不到指定的已發佈房源，則拋出此異常
     * @return 房源評論列表
     */
    public List<PropertyReviewVo> listPublishedPropertyReview(Long propertyId) {
        // 檢查房源是否存在及已發佈
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with propertyId %s is not found".formatted(propertyId));
        }

        // 根據房源ID查詢相應的評論列表並返回
        return propertyReviewVoMapper.listByPropertyId(propertyId);
    }
}
