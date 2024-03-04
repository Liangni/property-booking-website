package com.penny.vo.base;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingAvailabilityBaseVo {
    private Long bookingAvailabilityId;

    private String bookingAvailabilityStatus;

    private Date bookingAvailabilityDate;

    private Long propertyId;
}