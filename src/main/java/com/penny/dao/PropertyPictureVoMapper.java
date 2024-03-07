package com.penny.dao;

import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.PropertyPictureBaseVo;

import java.util.List;

public interface PropertyPictureVoMapper {
    List<PropertyPictureVo>  ListByPropertyIdList(List<Long> propertyIdList);

}
