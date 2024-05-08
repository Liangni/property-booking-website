package com.penny.service;

import com.penny.dao.BookingAvailabilityVoMapper;
import com.penny.dao.WishPropertyVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.WishPropertyBaseVoMapper;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceDuplicateException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.CreateWishPropertyRequest;
import com.penny.util.DateHelper;
import com.penny.vo.BookingAvailabilityVo;
import com.penny.vo.WishPropertyVo;
import com.penny.vo.base.PropertyBaseVo;
import com.penny.vo.base.WishPropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishPropertyService {
    private final EcUserService ecUserService;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final WishPropertyBaseVoMapper wishPropertyBaseVoMapper;

    private final WishPropertyVoMapper wishPropertyVoMapper;

    private final BookingAvailabilityVoMapper bookingAvailabilityVoMapper;

    private final DateHelper dateHelper;

    public void createWishProperty(CreateWishPropertyRequest createRequest){
        Long propertyId = createRequest.getPropertyId();
        LocalDate checkinDate = dateHelper.parseDateString(createRequest.getCheckinDateString());
        LocalDate checkoutDate = dateHelper.parseDateString(createRequest.getCheckoutDateString());

        // 檢查 checkin 日期是否早於 checkout 日期
        if(checkinDate.isAfter(checkoutDate)) {
            throw new RequestValidationException("checkinDate should be before checkoutDate");
        }

        // 檢查 checkin 日期是否是未來日期
        if (checkinDate.isBefore(LocalDate.now())){
            throw new RequestValidationException("checkinDate should be later than today");
        }

        // 檢查 property 是否存在
        PropertyBaseVo existingProperty = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property with id %s not found".formatted(propertyId)));

        // 檢查 property 是否是公開的 property
        if (!existingProperty.getIsPublished()) throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));


        // 檢查存在相同 property 的心願單
        WishPropertyVo existingWishPropertyVo = wishPropertyVoMapper.selectByPropertyId(propertyId);
        if (existingWishPropertyVo != null) throw new ResourceDuplicateException("wish property with propertyId %s already exists".formatted(propertyId));


        // 檢查預定時間是否有效
        // 檢查資料庫中的預訂日期是否足夠覆蓋指定日期範圍
        List<BookingAvailabilityVo> bookingAvailabilityVoList = bookingAvailabilityVoMapper.listByStartAndEndDate(propertyId, checkinDate, checkoutDate);
        if (bookingAvailabilityVoList.size() < dateHelper.countDayDifference(checkinDate, checkoutDate)) {
            // 生成指定日期範圍內的連續日期列表
            List<LocalDate> bookingLocalDateList = dateHelper.generateDateRange(checkinDate, checkoutDate);
            // 找出資料庫缺少的預訂日期
            List<LocalDate> dateMissingFromDb = dateHelper.listMissingBookingDate(bookingLocalDateList, bookingAvailabilityVoList);
            // 拋出異常，指示找不到足夠日期進行預訂
            throw new ResourceNotFoundException("%s are not available for booking".formatted(dateMissingFromDb));
        }

        for(BookingAvailabilityVo bookingDate : bookingAvailabilityVoList) {
            // 檢查日期可預訂性
            boolean isBooked = bookingDate.getBookingAvailabilityStatus().equals("booked");
            if (isBooked) throw new RequestValidationException("date range %s has been booked");
        }

        WishPropertyBaseVo wishPropertyBaseVo = WishPropertyBaseVo
                .builder()
                .propertyId(createRequest.getPropertyId())
                .checkinDate(checkinDate)
                .checkinDate(checkoutDate)
                .ecUserId(ecUserService.getLoginUser().getEcUserId())
                .build();

        wishPropertyBaseVoMapper.insertSelective(wishPropertyBaseVo);
    }
}
