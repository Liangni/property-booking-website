package com.penny.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateBookingOrderRequestDTO {

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private String guestName;

    private String guestEmail;

    private String guestPhone;

    private String arrivalTime;

    private String guestMessage;

    private Long propertyId;
}
