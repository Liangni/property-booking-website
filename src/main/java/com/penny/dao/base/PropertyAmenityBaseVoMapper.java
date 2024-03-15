package com.penny.dao.base;

import com.penny.vo.base.PropertyAmenityBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyAmenityBaseVoMapper {
    int deleteByPrimaryKey(Long propertyAmenityId);

    int insert(PropertyAmenityBaseVo record);

    int insertSelective(PropertyAmenityBaseVo record);

    PropertyAmenityBaseVo selectByPrimaryKey(Long propertyAmenityId);

    int updateByPrimaryKeySelective(PropertyAmenityBaseVo record);

    int updateByPrimaryKey(PropertyAmenityBaseVo record);
}