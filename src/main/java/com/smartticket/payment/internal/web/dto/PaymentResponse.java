package com.smartticket.payment.internal.web.dto;

import com.smartticket.payment.internal.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        BigDecimal amount,
        PaymentStatus status,
        Instant createdAt,
        Instant paidAt,
        String paymentUrl
) {
}
