package com.penny.vo.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistrictBaseVo {
    private Long districtId;

    private String districtName;

    private Long administrativeAreaId;

    private Long parentDistrictId;
}