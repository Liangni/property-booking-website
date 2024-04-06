package com.penny.dao;

import com.penny.vo.AmenityVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AmenityVoMapper {
    List<AmenityVo> listByPropertyId(Long propertyId);
}
