package com.penny.request.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressCreateRequest {
    private Long districtId;
    private String street;
}
