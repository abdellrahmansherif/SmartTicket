package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class OrderNotFoundForPaymentException extends NotFoundException {

    public OrderNotFoundForPaymentException(UUID orderId) {
        super("Order not found with id: " + orderId);
    }
}
