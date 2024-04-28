package com.penny.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePropertyAmenityRequest {
    private Long propertyId;

    private Long amenityId;
}
