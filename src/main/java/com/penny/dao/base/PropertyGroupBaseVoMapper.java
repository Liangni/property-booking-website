package com.penny.dao.base;

import com.penny.vo.base.PropertyGroupBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyGroupBaseVoMapper {
    int deleteByPrimaryKey(Long propertyGroupId);

    int insert(PropertyGroupBaseVo record);

    int insertSelective(PropertyGroupBaseVo record);

    PropertyGroupBaseVo selectByPrimaryKey(Long propertyGroupId);

    int updateByPrimaryKeySelective(PropertyGroupBaseVo record);

    int updateByPrimaryKey(PropertyGroupBaseVo record);
}