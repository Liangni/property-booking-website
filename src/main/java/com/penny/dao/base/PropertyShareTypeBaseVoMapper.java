package com.penny.dao.base;

import com.penny.vo.base.PropertyShareTypeBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyShareTypeBaseVoMapper {
    int deleteByPrimaryKey(Long propertyShareTypeId);

    int insert(PropertyShareTypeBaseVo record);

    int insertSelective(PropertyShareTypeBaseVo record);

    PropertyShareTypeBaseVo selectByPrimaryKey(Long propertyShareTypeId);

    int updateByPrimaryKeySelective(PropertyShareTypeBaseVo record);

    int updateByPrimaryKey(PropertyShareTypeBaseVo record);
}