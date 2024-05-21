package com.penny.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class ReadInitialPagePropertyResponse {
    private Long propertyId;

    private String propertyTitle;

    private Integer priceOnWeekdays;

    private Integer priceOnWeekends;

    private Double averageRating;

    private Integer reviewCount;

    private Long districtId;

    private String districtName;

    private Long parentDistrictId;

    private String parentDistrictName;

    private List<Map<String, Object>> propertyImageList;

    private LocalDate startAvailableDate;

    private LocalDate endAvailableDate;
}
