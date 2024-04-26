package com.penny.dao;

import com.penny.vo.AmenityVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AmenityVoMapper {
    /**
     * 根據設施類型 ID 列出特色設施。
     *
     * @param amenityTypeId 設施類型 ID
     * @return 特色設施列表
     */
    List<AmenityVo> listByAmenityTypeId(Long amenityTypeId);

    /**
     * 根據房源 ID 列出設施。
     *
     * @param propertyId 房源 ID
     * @return 設施列表
     */
    List<AmenityVo> listByPropertyId(Long propertyId);
}
