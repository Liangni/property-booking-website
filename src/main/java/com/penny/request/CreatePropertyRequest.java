package com.penny.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePropertyRequest {
    @NotNull
    private String propertyTitle;

    private String propertyDescription;

    @Positive(message = "maxNumOfGuests must be a positive integer")
    private Integer maxNumOfGuests;

    @Positive(message = "numOfBedrooms must be a positive integer")
    private Integer numOfBedrooms;

    @Positive(message = "numOfBeds must be a positive integer")
    private Integer numOfBeds;

    @Positive(message = "numOfBathrooms must be a positive integer")
    private Integer numOfBathrooms;

    @Positive(message = "priceOnWeekdays must be a positive integer")
    private Integer priceOnWeekdays;

    @Positive(message = "priceOnWeekends must be a positive integer")
    private Integer priceOnWeekends;

    private Boolean isPublished;

    private Long propertyMainSubTypeId;

    private Long propertyShareTypeId;

    private Long addressId;
}
