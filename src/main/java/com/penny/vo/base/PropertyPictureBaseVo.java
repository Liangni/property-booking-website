package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPictureBaseVo {
    private Long propertyPictureId;

    private Long propertyId;

    private Long pictureId;

    private Integer propertyPictureOrder;
}