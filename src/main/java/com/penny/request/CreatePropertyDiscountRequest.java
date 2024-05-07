package com.penny.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePropertyDiscountRequest {
    @Positive(message = "leastNumOfBookingDays must be a positive integer")
    private Integer leastNumOfBookingDays;

    @Positive(message = "discountValue must be a positive number")
    private Double discountValue;
}
