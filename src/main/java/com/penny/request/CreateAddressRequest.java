package com.penny.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAddressRequest {
    @NotNull
    private Long districtId;

    @NotNull
    private String street;
}
