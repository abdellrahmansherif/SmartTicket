package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class PaymentNotFoundException extends NotFoundException {

    public PaymentNotFoundException(UUID paymentId) {
        super("Payment not found with id: " + paymentId);
    }
}
