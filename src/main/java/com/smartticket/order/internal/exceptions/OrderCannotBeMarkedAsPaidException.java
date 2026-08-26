package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class OrderCannotBeMarkedAsPaidException extends ConflictException {
    public OrderCannotBeMarkedAsPaidException(String message) {
        super("Order Cannot Be Marked As Paid Exception"+message);
    }
}
