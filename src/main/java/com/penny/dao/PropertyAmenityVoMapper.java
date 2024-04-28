package com.penny.dao;

import com.penny.vo.PropertyAmenityVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyAmenityVoMapper {
    /**
     * 根據房源 ID 和設施 ID，從資料庫中查詢對應的房源設施信息。
     *
     * @param propertyId 房源 ID，用於查詢相關的房源設施。
     * @param amenityId 設施 ID，用於查詢相關的房源設施。
     * @return 返回對應的 PropertyAmenityVo 物件。
     */
    PropertyAmenityVo selectByPropertyIdAndAmenityId(Long propertyId, Long amenityId);
}
