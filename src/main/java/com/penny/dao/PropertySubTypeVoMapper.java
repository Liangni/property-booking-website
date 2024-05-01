package com.penny.dao;

import com.penny.vo.PropertySubTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertySubTypeVoMapper {

    /**
     * 根據房源主類型 ID 獲取房源子類型列表。
     *
     * @param propertyMainTypeId 房源主類型 ID
     * @return 房源類型列表
     */
    List<PropertySubTypeVo> listByPropertyMainTypeId(Long propertyMainTypeId);
}
