package com.penny.service;

import com.penny.dao.PropertySubTypeVoMapper;
import com.penny.vo.PropertySubTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertySubTypeService {
    private final PropertySubTypeVoMapper propertySubTypeVoMapper;

    /**
     * 根據房源主類型 ID 獲取房產類型列表。
     *
     * @param propertyMainTypeId 房源房源主類型 ID
     * @return 房源子類型列表
     */
    public List<PropertySubTypeVo> getByPropertyMainTypId(Long propertyMainTypeId) {
        return propertySubTypeVoMapper.listByPropertyMainTypeId(propertyMainTypeId);
    }

}
