package com.penny.service;

import com.penny.dao.BedroomVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.BedroomVo;
import com.penny.vo.base.BedroomBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BedroomService {
    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final BedroomVoMapper bedroomVoMapper;

    /**
     * 獲取已發佈的房源的臥室列表。
     *
     * @param propertyId 房源ID
     * @return 臥室列表
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發布，則拋出此異常
     */
    public List<BedroomVo> listPublishedPropertyBedrooms(Long propertyId) {
        // 根據房源ID查詢相應的房源信息
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);

        // 檢查房源是否存在及已發佈
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("propertyId %s not found".formatted(propertyId));
        }

        // 根據房源ID查詢相應的臥室列表並返回
        return bedroomVoMapper.listByPropertyId(propertyId);
    }
}
