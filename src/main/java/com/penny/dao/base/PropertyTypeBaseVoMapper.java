package com.penny.dao.base;

import com.penny.vo.base.PropertyTypeBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyTypeBaseVoMapper {
    int deleteByPrimaryKey(Long propertyTypeId);

    int insert(PropertyTypeBaseVo record);

    int insertSelective(PropertyTypeBaseVo record);

    PropertyTypeBaseVo selectByPrimaryKey(Long propertyTypeId);

    int updateByPrimaryKeySelective(PropertyTypeBaseVo record);

    int updateByPrimaryKey(PropertyTypeBaseVo record);
}