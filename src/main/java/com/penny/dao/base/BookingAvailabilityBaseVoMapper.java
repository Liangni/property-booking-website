package com.penny.dao.base;

import com.penny.vo.base.BookingAvailabilityBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookingAvailabilityBaseVoMapper {
    int deleteByPrimaryKey(Long bookingAvailabilityId);

    int insert(BookingAvailabilityBaseVo record);

    int insertSelective(BookingAvailabilityBaseVo record);

    BookingAvailabilityBaseVo selectByPrimaryKey(Long bookingAvailabilityId);

    int updateByPrimaryKeySelective(BookingAvailabilityBaseVo record);

    int updateByPrimaryKey(BookingAvailabilityBaseVo record);
}