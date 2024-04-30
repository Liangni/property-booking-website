package com.penny.dao;


import com.penny.vo.PropertyDiscountVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyDiscountVoMapper {
    /**
     * 根據房源 ID 和折扣 ID 查詢房源折扣資訊的方法。
     *
     * @param propertyId 房源 ID，用於指定要查詢的房源
     * @param discountId 折扣 ID，用於指定要查詢的折扣
     * @return 返回包含房源折扣資訊的 PropertyDiscountVo 物件，如果未找到則返回 null
     */
    PropertyDiscountVo selectByPropertyIdAndDiscountId(Long propertyId, Long discountId);
}
