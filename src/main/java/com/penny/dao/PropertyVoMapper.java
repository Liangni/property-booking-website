package com.penny.dao;

import com.penny.daoParameter.ListByNumOfAvailableDaysParam;
import com.penny.vo.PropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyVoMapper {
    /**
     * 依可預定天數查詢房源列表
     *
     * @param listByNumOfAvailableDaysParam 包含查詢條件的參數物件
     *
     * @return 符合條件的房源列表
     */
    List<PropertyVo> listByNumOfAvailableDays(ListByNumOfAvailableDaysParam listByNumOfAvailableDaysParam);
}
