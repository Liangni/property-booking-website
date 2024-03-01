package com.penny.dao.base;

import com.penny.vo.base.DistrictBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistrictBaseVoMapper {
    int deleteByPrimaryKey(Long districtId);
    int insert(DistrictBaseVo record);

    int insertSelective(DistrictBaseVo record);

    DistrictBaseVo selectByPrimaryKey(Long districtId);

    int updateByPrimaryKeySelective(DistrictBaseVo record);

    int updateByPrimaryKey(DistrictBaseVo record);
}