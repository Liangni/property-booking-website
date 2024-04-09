package com.penny.dao;

import com.penny.vo.PropertyReviewVo;

import java.util.List;
import java.util.Map;

public interface PropertyReviewVoMapper {
    /**
     * 根據房源ID列出房屋評論。
     *
     * @param propertyId 房源ID
     * @return 房源評論列表
     */
    List<PropertyReviewVo> listByPropertyId(Long propertyId);
}
