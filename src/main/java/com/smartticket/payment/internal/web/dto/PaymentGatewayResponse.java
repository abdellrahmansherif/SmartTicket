package com.smartticket.payment.internal.web.dto;

public record PaymentGatewayResponse(
        String transactionId,
        boolean success,
        String paymentUrl
) {
}
