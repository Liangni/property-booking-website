package com.penny.dao;

import java.util.List;
import java.util.Map;

public interface PropertyReviewVoMapper {
    /**
     * 根據房源ID列表計算評論數量
     *
     * @param propertyIdList 房源ID列表
     * @return 與提供房源ID相關的房源評論的計數
     */
    List<Map<String, Object>> countByPropertyIdList(List<Long> propertyIdList);
    /**
     * 根據房源ID計算評論數量
     *
     * @param propertyId 房源ID，用於指定要統計的房源
     * @return 以 Map 形式返回統計結果，其中鍵是房源ID，值是評論數量
     */
    Long countByPropertyId(Long propertyId);
}
