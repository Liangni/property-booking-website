package com.penny.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SearchPropertyRequest {
    private Integer numOfAvailableDays;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "startAvailableDateString must be in the format yyyy-MM-dd")
    private String startAvailableDateString;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "endAvailableDateString must be in the format yyyy-MM-dd")
    private String endAvailableDateString;

    @Positive(message = "maxNumOfGuests must be a positive integer")
    private Integer numOfGuests;

    @Positive(message = "numOfBedrooms must be a positive integer")
    private Integer numOfBedrooms;

    @Positive(message = "numOfBeds must be a positive integer")
    private Integer numOfBeds;

    @Positive(message = "numOfBathrooms must be a positive integer")
    private Integer numOfBathrooms;

    @Positive(message = "maxPrice must be a positive integer")
    private Integer maxPrice;

    @PositiveOrZero(message = "minPrice must be a positive integer or zero")
    private Integer minPrice;

    private Long propertyMainTypeId;

    private Long propertyShareTypeId;

    private List<Long> amenityIds;

    private Long districtId;
}
