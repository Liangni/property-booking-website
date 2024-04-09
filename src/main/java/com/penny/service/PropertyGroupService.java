package com.penny.service;

import com.penny.dao.PropertyGroupVoMapper;
import com.penny.vo.PropertyGroupVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyGroupService {
    private final PropertyGroupVoMapper propertyGroupVoMapper;

    /**
     * 獲取所有房源群組的資訊。
     *
     * @return 包含所有房源群組資訊的列表
     */
    public List<PropertyGroupVo> getPropertyGroups() {
        return propertyGroupVoMapper.listAll();
    }
}
