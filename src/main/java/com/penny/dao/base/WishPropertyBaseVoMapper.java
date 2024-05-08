package com.penny.dao.base;

import com.penny.vo.base.WishPropertyBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WishPropertyBaseVoMapper {
    int deleteByPrimaryKey(Long wishPropertyId);

    int insert(WishPropertyBaseVo record);

    int insertSelective(WishPropertyBaseVo record);

    WishPropertyBaseVo selectByPrimaryKey(Long wishPropertyId);

    int updateByPrimaryKeySelective(WishPropertyBaseVo record);

    int updateByPrimaryKey(WishPropertyBaseVo record);
}