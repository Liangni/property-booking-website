package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BedroomBaseVo {
    private Long bedroomId;

    private Integer numOfSingleBeds;

    private Integer numOfDoubleBeds;

    private Long propertyId;
}