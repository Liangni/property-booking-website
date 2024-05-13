package com.penny.service;

import com.penny.dao.BookingCalendarVoMapper;
import com.penny.dao.WishPropertyVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.WishPropertyBaseVoMapper;
import com.penny.exception.AuthorizationException;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceDuplicateException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.CreateWishPropertyRequest;
import com.penny.util.DateHelper;
import com.penny.vo.BookingCalendarVo;
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

    private final BookingCalendarVoMapper bookingCalendarVoMapper;

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

        // 檢查日期是否可預訂
        List<BookingCalendarVo> bookingDateList = bookingCalendarVoMapper.listByStartAndEndDate(propertyId, checkinDate, checkoutDate);
        if (!bookingDateList.isEmpty()) {
            throw new RequestValidationException("date range %s has been booked".formatted(
                    bookingDateList.stream().map(BookingCalendarVo::getBookingDate).toList())
            );
        }

        WishPropertyBaseVo wishPropertyBaseVo = WishPropertyBaseVo
                .builder()
                .propertyId(createRequest.getPropertyId())
                .checkinDate(checkinDate)
                .checkoutDate(checkoutDate)
                .ecUserId(ecUserService.getLoginUser().getEcUserId())
                .build();

        wishPropertyBaseVoMapper.insertSelective(wishPropertyBaseVo);
    }

    /**
     * 根據使用者 ID 列出願望房源列表。
     *
     * @param ecUserId 使用者 ID
     * @return 返回願望房源列表
     * @throws AuthorizationException 如果登錄使用者無權執行操作，則拋出授權異常
     */
    public List<WishPropertyVo> listWishPropertyByEcUserId(Long ecUserId) {
        if (!ecUserId.equals(ecUserService.getLoginUser().getEcUserId())) {
            throw new AuthorizationException("login user is not authorized to do the operation");
        }

        return wishPropertyVoMapper.listByEcUserId(ecUserId);
    }

    /**
     * 根據願望房源 ID 刪除願望房源。
     *
     * @param wishPropertyId 要刪除的願望房源 ID
     * @throws AuthorizationException 如果登錄使用者無權執行操作，則拋出授權異常
     */
    public void deleteWishPropertyById(Long wishPropertyId) {
        WishPropertyBaseVo wishPropertyBaseVo = wishPropertyBaseVoMapper.selectByPrimaryKey(wishPropertyId);

        if (!wishPropertyBaseVo.getEcUserId().equals(ecUserService.getLoginUser().getEcUserId())) {
            throw new AuthorizationException("login user is not authorized to do the operation");
        }

        wishPropertyBaseVoMapper.deleteByPrimaryKey(wishPropertyId);
    }
}
