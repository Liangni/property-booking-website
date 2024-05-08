package com.penny.vo.base;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishPropertyBaseVo {
    private Long wishPropertyId;

    private Long propertyId;

    private Long ecUserId;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;
}