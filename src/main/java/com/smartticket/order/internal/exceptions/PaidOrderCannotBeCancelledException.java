package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class PaidOrderCannotBeCancelledException extends ConflictException {

    public PaidOrderCannotBeCancelledException() {
        super("Paid order cannot be cancelled directly");
    }
}
