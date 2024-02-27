package com.penny.dao;

import com.penny.vo.DistrictVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DistrictVoMapper {
    List<DistrictVo> selectByNameKeyword(String districtName, Integer offset, Integer limit);

    Integer countSelectByNameKeyword(String districtName);
}
