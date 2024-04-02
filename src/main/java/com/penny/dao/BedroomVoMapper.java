package com.penny.dao;

import com.penny.vo.BedroomVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BedroomVoMapper {
    /**
     * 根據房源ID列出臥室列表。
     *
     * @param propertyId 要查詢的房源 ID。
     * @return 返回一個列表，其中包含與給定房源 ID 相關的所有臥室信息。
     */
    List<BedroomVo> listByPropertyId(Long propertyId);

}
