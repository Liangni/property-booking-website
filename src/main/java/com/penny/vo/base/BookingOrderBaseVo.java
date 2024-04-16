package com.penny.vo.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingOrderBaseVo {
    private Long bookingOrderId;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private String guestName;

    private String guestEmail;

    private String guestPhone;

    private String arrivalTime;

    private String guestMessage;

    private BigDecimal orderTotal;

    private Long propertyId;

    private Long hostId;

    private Long customerId;

    private Long discountId;
}