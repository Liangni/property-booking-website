package com.penny.dao;

import java.util.List;
import java.util.Map;

public interface PropertyReviewVoMapper {
    /**
     * 根據房源ID計算數量。
     *
     * @param propertyIdList 房源ID列表
     * @return 與提供房源ID相關的房源評論的計數
     */
    List<Map<String, Object>> countByPropertyIdList(List<Long> propertyIdList);
}
