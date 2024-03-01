package com.penny.dao.base;

import com.penny.vo.base.PropertyBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyBaseVoMapper {
    int deleteByPrimaryKey(Long propertyId);

    int insert(PropertyBaseVo record);

    int insertSelective(PropertyBaseVo record);

    PropertyBaseVo selectByPrimaryKey(Long propertyId);

    int updateByPrimaryKeySelective(PropertyBaseVo record);

    int updateByPrimaryKey(PropertyBaseVo record);
}