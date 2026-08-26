package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class OrderAlreadyCancelledException extends ConflictException {

    public OrderAlreadyCancelledException() {
        super("Order is already cancelled");
    }
}
