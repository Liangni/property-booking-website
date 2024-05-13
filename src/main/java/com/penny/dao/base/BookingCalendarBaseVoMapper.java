package com.penny.dao.base;

import com.penny.vo.base.BookingCalendarBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookingCalendarBaseVoMapper {
    int deleteByPrimaryKey(Long bookingCalendarId);

    int insert(BookingCalendarBaseVo record);

    int insertSelective(BookingCalendarBaseVo record);

    BookingCalendarBaseVo selectByPrimaryKey(Long bookingCalendarId);

    int updateByPrimaryKeySelective(BookingCalendarBaseVo record);

    int updateByPrimaryKey(BookingCalendarBaseVo record);
}