package com.penny.dao.base;

import com.penny.vo.base.PropertySubTypeBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertySubTypeBaseVoMapper {
    int deleteByPrimaryKey(Long propertySubTypeId);

    int insert(PropertySubTypeBaseVo record);

    int insertSelective(PropertySubTypeBaseVo record);

    PropertySubTypeBaseVo selectByPrimaryKey(Long propertySubTypeId);

    int updateByPrimaryKeySelective(PropertySubTypeBaseVo record);

    int updateByPrimaryKey(PropertySubTypeBaseVo record);
}