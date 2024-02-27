package com.penny.dao;

import com.penny.vo.DistrictVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrictVoMapper {
    List<DistrictVo> selectByNameKeyword(String districtName);
}
