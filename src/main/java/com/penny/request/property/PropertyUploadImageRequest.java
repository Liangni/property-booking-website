package com.penny.request.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUploadImageRequest {
    private Long propertyId;

    private String fileExtension;
}
