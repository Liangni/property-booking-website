package com.penny.dao;

import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.vo.PropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyVoMapper {
    /**
     * 依可預定天數查詢房源列表
     *
     * @param selectPropertyParam 包含查詢條件的參數物件
     *
     * @return 符合條件的房源列表
     */
    List<PropertyVo> listByNumOfAvailableDays(SelectPropertyParam selectPropertyParam);
    /**
     * 根據指定的可預定天數統計房源的數量
     *
     * @param numOfAvailableDay 指定的可預定天數，用於統計房源數量
     * @return 返回指定可預定天數的房源数量
     */
    Long countByNumOfAvailableDays(Integer numOfAvailableDay);
    List<PropertyVo> listByStartAndEndAvailableDay(SelectPropertyParam selectPropertyParam);
}
