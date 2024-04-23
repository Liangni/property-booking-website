package com.penny.dao;

import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.PropertyPictureBaseVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyPictureVoMapper {
    /**
     * 根據房源ID列表查詢房源圖片
     *
     * @param propertyIdList 房源ID列表
     * @return 符合房源ID列表的房源圖片列表
     */
    List<PropertyPictureVo>  listByPropertyIdList(List<Long> propertyIdList);

    /**
     * 根據房源ID列出房源圖片列表。
     *
     * @param propertyId 房源ID，用於查詢相關的房源圖片。
     * @return 返回一個列表，其中包含與給定房源ID相關的所有房源圖片。
     */
    List<PropertyPictureVo> listByPropertyId(Long propertyId);

    PropertyPictureVo selectByPropertyIdAndPictureOrder(Long propertyId, Integer pictureOrder);
}
