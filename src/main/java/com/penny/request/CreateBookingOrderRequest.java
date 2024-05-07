package com.penny.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookingOrderRequest {

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "checkinDate must be in the format yyyy-MM-dd")
    private String checkinDateString;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "checkoutDate must be in the format yyyy-MM-dd")
    private String checkoutDateString;

    @NotNull
    private String guestName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z]{2,}$", message = "invalid email format")
    private String guestEmail;

    private String guestPhone;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message= "arrivalTime must be in the format HH:mm-HH:mm")
    private String arrivalTime;

    private String guestMessage;

    private Long propertyId;
}
