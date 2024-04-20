package com.penny.service;

import com.penny.dao.PropertyTypeVoMapper;
import com.penny.vo.PropertyTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyTypeService {
    private final PropertyTypeVoMapper propertyTypeVoMapper;

    /**
     * 根據房源群組 ID 獲取房產類型列表。
     *
     * @param propertyGroupId 房源群組 ID
     * @return 房源類型列表
     */
    public List<PropertyTypeVo> getByPropertyGroupId(Long propertyGroupId) {
        return propertyTypeVoMapper.listByPropertyGroupId(propertyGroupId);
    }

}
