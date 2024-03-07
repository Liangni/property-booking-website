package com.penny.dao.base;

import com.penny.vo.base.PropertyPictureBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyPictureBaseVoMapper {
    int deleteByPrimaryKey(Long propertyPictureId);

    int insert(PropertyPictureBaseVo record);

    int insertSelective(PropertyPictureBaseVo record);

    PropertyPictureBaseVo selectByPrimaryKey(Long propertyPictureId);

    int updateByPrimaryKeySelective(PropertyPictureBaseVo record);

    int updateByPrimaryKey(PropertyPictureBaseVo record);
}