package com.penny.request.bookingOrder;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BookingOrderCreateRequest {
    private String checkinDate;

    private String checkoutDate;

    private String guestName;

    private String guestEmail;

    private String guestPhone;

    private String arrivalTime;

    private String guestMessage;

    private Long propertyId;
}
