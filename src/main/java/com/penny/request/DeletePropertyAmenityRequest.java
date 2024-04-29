package com.penny.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletePropertyAmenityRequest {
    private Long propertyId;

    private Long amenityId;
}
