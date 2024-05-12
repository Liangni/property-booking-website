package com.penny.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


@Data
public class ConfirmPaymentRequest {
    @NotNull
    private Long bookingOrderId;

    @NotNull
    private Boolean isPaymentSuccessful;
}
