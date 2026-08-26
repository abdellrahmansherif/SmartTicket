package com.smartticket.payment.internal.gateway;

import com.smartticket.payment.internal.web.dto.PaymentGatewayResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGateway {

    PaymentGatewayResponse createPayment(
            UUID orderId,
            BigDecimal amount
    );

}
