package com.penny.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SearchPropertyRequestDTO {
    private Integer numOfAvailableDays;

    private LocalDate startAvailableDate;

    private LocalDate endAvailableDate;

    private Integer numOfGuests;

    private Integer numOfBedrooms;

    private Integer numOfBeds;

    private Integer numOfBathrooms;

    private Integer maxPrice;

    private Integer minPrice;

    private Long propertyMainTypeId;

    private Long propertyShareTypeId;

    private List<Long> amenityIds;

    private Long districtId;
}
