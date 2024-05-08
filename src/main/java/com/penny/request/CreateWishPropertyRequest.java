package com.penny.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateWishPropertyRequest {

    @NotNull
    private Long propertyId;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "checkinDateString must be in the format yyyy-MM-dd")
    private String checkinDateString;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "checkoutDateString must be in the format yyyy-MM-dd")
    private String checkoutDateString;
}
