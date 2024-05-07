package com.penny.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePropertyRequest {
    @NotNull
    private String propertyTitle;

    private String propertyDescription;

    @Positive(message = "Value must be a positive integer")
    private Integer maxNumOfGuests;

    @Positive(message = "Value must be a positive integer")
    private Integer numOfBedrooms;

    @Positive(message = "Value must be a positive integer")
    private Integer numOfBeds;

    @Positive(message = "Value must be a positive integer")
    private Integer numOfBathrooms;

    @Positive(message = "Value must be a positive integer")
    private Integer priceOnWeekdays;

    @Positive(message = "Value must be a positive integer")
    private Integer priceOnWeekends;

    private Boolean isPublished;

    @Positive(message = "Value must be a positive integer")
    private Long propertyMainSubTypeId;

    @Positive(message = "Value must be a positive integer")
    private Long propertyShareTypeId;

    @Positive(message = "Value must be a positive integer")
    private Long addressId;
}
