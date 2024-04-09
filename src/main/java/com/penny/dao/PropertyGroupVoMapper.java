package com.penny.dao;

import com.penny.vo.PropertyGroupVo;
import com.penny.vo.PropertyPictureVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyGroupVoMapper {
    List<PropertyGroupVo> listAll();
}
