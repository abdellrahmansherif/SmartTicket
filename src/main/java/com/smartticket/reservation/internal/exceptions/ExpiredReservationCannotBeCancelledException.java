package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class ExpiredReservationCannotBeCancelledException extends ConflictException {

    public ExpiredReservationCannotBeCancelledException() {
        super("Expired reservation cannot be cancelled");
    }
}
