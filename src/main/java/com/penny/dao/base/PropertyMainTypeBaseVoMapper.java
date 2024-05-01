package com.penny.dao.base;

import com.penny.vo.base.PropertyMainTypeBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyMainTypeBaseVoMapper {
    int deleteByPrimaryKey(Long propertyMainTypeId);

    int insert(PropertyMainTypeBaseVo record);

    int insertSelective(PropertyMainTypeBaseVo record);

    PropertyMainTypeBaseVo selectByPrimaryKey(Long propertyMainTypeId);

    int updateByPrimaryKeySelective(PropertyMainTypeBaseVo record);

    int updateByPrimaryKey(PropertyMainTypeBaseVo record);
}