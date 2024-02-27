package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistrictBaseVo {
    private Long districtId;

    private String districtName;

    private Long administrativeAreaId;

    private Long parentDistrictId;
}