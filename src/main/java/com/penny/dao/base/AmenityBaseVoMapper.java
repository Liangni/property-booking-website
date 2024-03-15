package com.penny.dao.base;

import com.penny.vo.base.AmenityBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmenityBaseVoMapper {
    int deleteByPrimaryKey(Long amenityId);

    int insert(AmenityBaseVo record);

    int insertSelective(AmenityBaseVo record);

    AmenityBaseVo selectByPrimaryKey(Long amenityId);

    int updateByPrimaryKeySelective(AmenityBaseVo record);

    int updateByPrimaryKey(AmenityBaseVo record);
}