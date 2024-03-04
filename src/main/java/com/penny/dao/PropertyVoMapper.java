package com.penny.dao;

import com.penny.vo.PropertyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyVoMapper {
    /**
     * 依可預定天數查詢房源列表
     *
     * @param numOfAvailableDay 連續可預訂天數
     * @param returnFieldList   要返回的欄位名稱列表，可接受的字串包括：
     *                           - "propertyId": 房源 ID
     *                           - "propertyTitle": 房源標題
     *                           - "propertyDescription": 房源描述
     *                           - "numOfBedrooms": 卧室數量
     *                           - "numOfBeds": 床舖數量
     *                           - "numOfBathrooms": 浴室數量
     *                           - "priceOnWeekdays": 工作日價格
     *                           - "priceOnWeekends": 周末價格
     *                           - "isPublished": 是否已發布
     *                           - "averageRating": 平均評分
     *                           - "propertyGroupTypeId": 房源组類型 ID
     *                           - "propertyShareTypeId": 房源共享類型 ID
     *                           - "addressId": 地址 ID
     *                           - "address": 地址
     *                           - "district": 區域
     * @param sortFieldList     排序欄位名稱列表，可接受的字串包括：
     *                           - "district": 按房源所在區域排序
     *                           - "nearestAvailableDay": 按最近可預訂日期排序
     * @param offset       查詢的偏移量
     * @param limit        查詢的限制數量
     *
     * @return 符合條件的房源列表
     */
    List<PropertyVo> listByNumOfAvailableDays(Integer numOfAvailableDay, List<String> returnFieldList, List<String> sortFieldList, Integer offset, Integer limit);
}
