package com.penny.dao.base;

import com.penny.vo.base.BookingOrderBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookingOrderBaseVoMapper {
    int deleteByPrimaryKey(Long bookingOrderId);

    int insert(BookingOrderBaseVo record);

    int insertSelective(BookingOrderBaseVo record);

    BookingOrderBaseVo selectByPrimaryKey(Long bookingOrderId);

    int updateByPrimaryKeySelective(BookingOrderBaseVo record);

    int updateByPrimaryKey(BookingOrderBaseVo record);
}