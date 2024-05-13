package com.penny.dao;

import com.penny.vo.BookingCalendarVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BookingCalendarVoMapper {
    /**
     * 根據房源ID列出預定日期。
     *
     * @param propertyId 房源ID
     * @return List<BookingCalendarVo> 預定日期的列表
     */
    List<BookingCalendarVo> listByPropertyId(Long propertyId);

    /**
     * 根據房源ID和開始日期、結束日期篩選預定日期列表。
     *
     * @param propertyId 房源ID
     * @param startDate  開始日期
     * @param endDate    結束日期
     * @return List<BookingCalendarVo> 符合條件的預定日期列表
     */
    List<BookingCalendarVo> listByStartAndEndDate(Long propertyId, LocalDate startDate, LocalDate endDate);
}
