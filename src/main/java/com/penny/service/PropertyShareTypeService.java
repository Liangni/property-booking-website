package com.penny.service;

import com.penny.dao.PropertyShareTypeVoMapper;
import com.penny.vo.PropertyShareTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyShareTypeService {

    private final PropertyShareTypeVoMapper propertyShareTypeVoMapper;

    /**
     * 獲取所有房源共享類型。
     *
     * @return 房源共享類型的列表
     */
    public List<PropertyShareTypeVo> getPropertyShareTypes() {
        return propertyShareTypeVoMapper.listAll();
    }
}
