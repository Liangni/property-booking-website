package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDiscountBaseVo {
    private Long propertyDiscountId;

    private Long propertyId;

    private Long discountId;
}