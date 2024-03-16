package com.penny.dao;

import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.vo.PropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface PropertyVoMapper {
    /**
     * 根據提供的篩選條件，查詢房源的列表。
     *
     * @param selectPropertyParam 包含用於篩選房源的參數物件
     * @return 符合指定篩選條件的物件列表
     */
    List<PropertyVo> listByFilter(SelectPropertyParam selectPropertyParam);
    /**
     * 根據提供的過濾條件計算記錄的數量。
     *
     * @param filterMap 包含過濾條件的映射
     *                  其中鍵是列名，值是過濾值
     * @return 符合過濾條件的記錄數量
     */
    Long countByFilter(Map<String, Object> filterMap);
}
