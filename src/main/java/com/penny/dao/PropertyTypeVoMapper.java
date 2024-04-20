package com.penny.dao;

import com.penny.vo.PropertyTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyTypeVoMapper {

    /**
     * 根據房源群組 ID 獲取房產類型列表。
     *
     * @param propertyGroupId 房源群組 ID
     * @return 房源類型列表
     */
    List<PropertyTypeVo> listByPropertyGroupId(Long propertyGroupId);
}
