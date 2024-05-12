package com.penny.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingOrderPaymentStatusEnum {
    PENDING("pending"),
    SUCCEED("succeed"),
    FAIL("fail");

    private final String displayName;
}
