package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyBaseVo {
    private Long propertyId;

    private String propertyTitle;

    private String propertyDescription;

    private Integer maxNumOfGuests;

    private Integer numOfBedrooms;

    private Integer numOfBeds;

    private Integer numOfBathrooms;

    private Integer priceOnWeekdays;

    private Integer priceOnWeekends;

    private Boolean isPublished;

    private Double averageRating;

    private Integer reviewCount;

    private Long propertyGroupTypeId;

    private Long propertyShareTypeId;

    private Long addressId;

    private Long hostId;
}