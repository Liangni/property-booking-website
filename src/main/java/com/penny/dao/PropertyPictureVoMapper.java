package com.penny.dao;

import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.PropertyPictureBaseVo;

import java.util.List;

public interface PropertyPictureVoMapper {
    /**
     * 根據房源ID列表查詢房源圖片
     *
     * @param propertyIdList 房源ID列表
     * @return 符合房源ID列表的房源圖片列表
     */
    List<PropertyPictureVo>  listByPropertyIdList(List<Long> propertyIdList);

}
