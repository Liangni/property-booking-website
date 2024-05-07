package com.penny.service;

import com.penny.dao.BookingAvailabilityVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.BookingAvailabilityVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingAvailabilityService {

    private final BookingAvailabilityVoMapper bookingAvailabilityVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    /**
     * 根據房源ID獲取預訂日期。
     *
     * @param propertyId 房源ID
     * @throws ResourceNotFoundException 如果找不到指定的已發佈房源，則拋出此異常
     * @return List<BookingAvailabilityVo> 房源預訂日期列表
     */
    public List<BookingAvailabilityVo> listPublishedPropertyBookingAvailability(Long propertyId) {
        // 檢查房源是否存在及已發佈
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null || !propertyBaseVo.getIsPublished()) {
            throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));
        }

        // 根據房源ID查詢相應的訂房日期列表並返回
        return bookingAvailabilityVoMapper.listByPropertyId(propertyId);
    }
}
