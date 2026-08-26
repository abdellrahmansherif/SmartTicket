package com.smartticket.payment.internal.web.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(
        @NotNull(message = "Order id is required")
        UUID orderId,


        BigDecimal amount
) {
}
