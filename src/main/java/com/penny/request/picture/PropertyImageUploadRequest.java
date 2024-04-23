package com.penny.request.picture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyImageUploadRequest {
    private Long propertyId;

    private String fileExtension;

    private Integer pictureOrder;
}
