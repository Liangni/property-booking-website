package com.penny.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReadEcUserPropertyResponse {

    private Long propertyId;

    private String propertyTitle;

    private Long districtId;

    private String districtName;

    private Long parentDistrictId;

    private String parentDistrictName;
}
