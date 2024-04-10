package com.penny.service;

import com.penny.dao.BookingAvailabilityVoMapper;
import com.penny.vo.BookingAvailabilityVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingAvailabilityService {

    private final BookingAvailabilityVoMapper bookingAvailabilityVoMapper;

    /**
     * 根據房源ID獲取預訂日期。
     *
     * @param propertyId 房源ID
     * @return List<BookingAvailabilityVo> 房源預訂日期列表
     */
    public List<BookingAvailabilityVo> listPropertyBookingAvailability(Long propertyId) {
        return bookingAvailabilityVoMapper.listByPropertyId(propertyId);
    }
}
