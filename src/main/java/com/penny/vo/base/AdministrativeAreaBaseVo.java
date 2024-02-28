package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeAreaBaseVo {
    private Long administrativeAreaId;

    private String administrativeAreaName;

    private Long administrativeAreaLevel;
}