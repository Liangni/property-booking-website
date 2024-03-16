package com.penny.dao;

import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.vo.PropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

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
     * 根據指定的可預定天數統計房源的數量
     *
     * @param numOfAvailableDay 指定的可預定天數，用於統計房源數量
     * @return 返回指定可預定天數的房源数量
     */
    Long countByNumOfAvailableDays(Integer numOfAvailableDay);
    /**
     * 根據開始可預定日期和結束可預定日期計算房源數量。
     *
     * @param startAvailableDate 房源的開始可預定日期
     * @param endAvailableDate   房源的結束可預定日期
     * @return 符合條件的房源數量
     */
    Long countByStartAndEndAvailableDate(LocalDate startAvailableDate, LocalDate endAvailableDate);

}
