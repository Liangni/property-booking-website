package com.penny.dao;

import com.penny.request.property.PropertySearchRequest;
import com.penny.vo.PropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyVoMapper {
    /**
     * 根據指定的可預訂天數搜尋房源。
     *
     * @param request 搜尋資訊，包含可預訂天數等資訊。
     * @return 符合條件的房源列表。
     */
    List<PropertyVo> listByNumOfAvailableDays(PropertySearchRequest request);

    /**
     * 根據指定的開始和結束可預訂日期搜尋房源。
     *
     * @param request 搜尋資訊，包含開始和結束可預訂日期等資訊。
     * @return 符合條件的房源列表。
     */
    List<PropertyVo> listByStartAndEndAvailableDate(PropertySearchRequest request);

    /**
     * 根據指定的房屋屬性搜尋房源。
     *
     * @param request 搜尋資訊，包含房屋屬性等資訊。
     * @return 符合條件的房源列表。
     */
    List<PropertyVo> listByPropertyAttributes(PropertySearchRequest request);

}
