package com.penny.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAddressRequest {
    private Long districtId;
    private String street;
}
