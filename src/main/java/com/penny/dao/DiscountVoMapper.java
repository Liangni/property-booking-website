package com.penny.dao;

import com.penny.vo.DiscountVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscountVoMapper {
    /**
     * 根據房源ID列出折扣資訊。
     *
     * @param propertyId 房源ID
     * @return 折扣資訊列表
     */
    List<DiscountVo> listByPropertyId(Long propertyId);
}
