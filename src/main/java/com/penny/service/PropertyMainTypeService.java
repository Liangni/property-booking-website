package com.penny.service;

import com.penny.dao.PropertyMainTypeVoMapper;
import com.penny.vo.PropertyMainTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyMainTypeService {
    private final PropertyMainTypeVoMapper propertyMainTypeVoMapper;

    /**
     * 獲取所有房源主類型的資訊。
     *
     * @return 包含所有房源主類型資訊的列表
     */
    public List<PropertyMainTypeVo> listPropertyMainType() {
        return propertyMainTypeVoMapper.listAll();
    }
}
