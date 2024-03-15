package com.penny.dao.base;

import com.penny.vo.base.AmenityTypeBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmenityTypeBaseVoMapper {
    int deleteByPrimaryKey(Long amenityTypeId);

    int insert(AmenityTypeBaseVo record);

    int insertSelective(AmenityTypeBaseVo record);

    AmenityTypeBaseVo selectByPrimaryKey(Long amenityTypeId);

    int updateByPrimaryKeySelective(AmenityTypeBaseVo record);

    int updateByPrimaryKey(AmenityTypeBaseVo record);
}