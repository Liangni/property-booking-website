package com.penny.dao;

import com.penny.vo.BookingAvailabilityVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
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

    /**
     * 根據房源ID和開始日期、結束日期篩選預定日期列表。
     *
     * @param propertyId 房源ID
     * @param startDate  開始日期
     * @param endDate    結束日期
     * @return 符合條件的預定日期列表
     */
    List<BookingAvailabilityVo> listByStartAndEndDate(Long propertyId, LocalDate startDate, LocalDate endDate);

    /**
     * 根據預訂預定日期ID列表設置為已預定狀態。
     *
     * @param bookingAvailabilityIdList 預定日期ID列表
     * @return 設置狀態的數量
     */
    int setStatusToBookingByPrimaryKeyList(List<Long> bookingAvailabilityIdList);
}
