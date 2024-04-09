package com.penny.dao;

import com.penny.vo.BookingAvailabilityVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookingAvailabilityVoMapper {
    /**
     * 根據房源ID列出預定日期。
     *
     * @param propertyId 房源ID
     * @return List<BookingAvailabilityVo> 預定日期的列表
     */
    List<BookingAvailabilityVo> listByPropertyId(Long propertyId);
}
