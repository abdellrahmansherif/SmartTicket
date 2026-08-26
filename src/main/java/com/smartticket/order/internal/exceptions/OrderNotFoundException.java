package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException(UUID orderId) {
        super("Order not found with id: " + orderId);
    }
}
