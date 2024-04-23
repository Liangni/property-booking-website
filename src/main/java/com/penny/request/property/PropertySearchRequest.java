package com.penny.request.property;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PropertySearchRequest {
    private Integer numOfAvailableDays;

    private String startAvailableDateString;

    private String endAvailableDateString;

    private LocalDate startAvailableDate;

    private LocalDate endAvailableDate;

    private Integer numOfGuests;

    private Integer numOfBedrooms;

    private Integer numOfBeds;

    private Integer numOfBathrooms;

    private Integer maxPrice;

    private Integer minPrice;

    private Long propertyGroupId;

    private Long propertyShareTypeId;

    private List<Long> amenityIds;

    private Long districtId;
}
