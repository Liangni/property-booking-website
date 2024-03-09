package com.penny.dao.base;

import com.penny.vo.base.PropertyReviewBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyReviewBaseVoMapper {
    int deleteByPrimaryKey(Long propertyReviewId);

    int insert(PropertyReviewBaseVo record);

    int insertSelective(PropertyReviewBaseVo record);

    PropertyReviewBaseVo selectByPrimaryKey(Long propertyReviewId);

    int updateByPrimaryKeySelective(PropertyReviewBaseVo record);

    int updateByPrimaryKey(PropertyReviewBaseVo record);
}