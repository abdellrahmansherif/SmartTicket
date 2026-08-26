package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class PaymentForOrderNotFoundException extends NotFoundException {

    public PaymentForOrderNotFoundException(UUID orderId) {
        super("Payment not found for order: " + orderId);
    }
}
