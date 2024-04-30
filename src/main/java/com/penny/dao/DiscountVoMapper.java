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

    /**
     * 根據折扣值和最少預訂天數查詢折扣資訊的方法。
     *
     * @param discountValue       折扣值
     * @param leastNumOfBookingDays 最少預訂天數
     * @return 返回包含折扣資訊的 DiscountVo 物件
     */
    DiscountVo selectByDiscountValueAndLeastNumOfBookingDays(Double discountValue, Integer leastNumOfBookingDays);
}
