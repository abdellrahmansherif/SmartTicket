package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class ConfirmedReservationCannotBeCancelledException extends ConflictException {

    public ConfirmedReservationCannotBeCancelledException() {
        super("Confirmed reservation cannot be cancelled");
    }
}
