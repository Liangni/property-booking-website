package com.penny.dao;

import com.penny.vo.PropertyMainTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyMainTypeVoMapper {
    List<PropertyMainTypeVo> listAll();
}
