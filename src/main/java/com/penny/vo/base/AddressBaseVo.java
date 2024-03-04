package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBaseVo {
    private Long addressId;

    private String apartmentFloor;

    private String street;

    private Long adminAreaLevel1DistrictId;

    private Long adminAreaLevel2DistrictId;

    private Long adminAreaLevel3DistrictId;
}