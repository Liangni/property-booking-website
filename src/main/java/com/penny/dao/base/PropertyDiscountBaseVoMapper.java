package com.penny.dao.base;

import com.penny.vo.base.PropertyDiscountBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyDiscountBaseVoMapper {
    int deleteByPrimaryKey(Long propertyDiscountId);

    int insert(PropertyDiscountBaseVo record);

    int insertSelective(PropertyDiscountBaseVo record);

    PropertyDiscountBaseVo selectByPrimaryKey(Long propertyDiscountId);

    int updateByPrimaryKeySelective(PropertyDiscountBaseVo record);

    int updateByPrimaryKey(PropertyDiscountBaseVo record);
}