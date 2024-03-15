package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyAmenityBaseVo {
    private Long propertyAmenityId;

    private Long propertyId;

    private Long amenityId;
}