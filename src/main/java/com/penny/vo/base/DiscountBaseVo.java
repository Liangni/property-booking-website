package com.penny.vo.base;

import java.time.LocalDateTime;
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

    private String discountName;

    private Integer leastNumOfBookingDays;

    private Double discountValue;

    private String discountUnit;

    private Boolean discountIsActive;

    private LocalDateTime discountCreatedAt;

    private Long propertyId;
}