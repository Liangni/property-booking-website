package com.penny.request.bookingOrder;

import lombok.Builder;
import lombok.Data;

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
