package com.penny.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePropertyPictureRequest {
    private Long pictureId;

    private Long propertyId;

    private Integer pictureOrder;
}
