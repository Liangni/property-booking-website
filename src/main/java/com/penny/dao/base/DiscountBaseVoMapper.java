package com.penny.dao.base;

import com.penny.vo.base.DiscountBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiscountBaseVoMapper {
    int deleteByPrimaryKey(Long discountId);

    int insert(DiscountBaseVo record);

    int insertSelective(DiscountBaseVo record);

    DiscountBaseVo selectByPrimaryKey(Long discountId);

    int updateByPrimaryKeySelective(DiscountBaseVo record);

    int updateByPrimaryKey(DiscountBaseVo record);
}