package com.penny.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookingOrderRequest {
    private String checkinDate;

    private String checkoutDate;

    private String guestName;

    private String guestEmail;

    private String guestPhone;

    private String arrivalTime;

    private String guestMessage;

    private Long propertyId;
}
