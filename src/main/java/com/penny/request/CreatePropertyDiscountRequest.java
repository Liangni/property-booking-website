package com.penny.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePropertyDiscountRequest {
    private Integer leastNumOfBookingDays;

    private Double discountValue;
}
