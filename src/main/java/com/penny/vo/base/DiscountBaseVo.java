package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountBaseVo {
    private Long discountId;

    private Integer leastNumOfBookingDays;

    private Double discountValue;
}