package com.penny.dao;

import com.penny.vo.PropertyShareTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyShareTypeVoMapper {
    List<PropertyShareTypeVo> listAll();
}
