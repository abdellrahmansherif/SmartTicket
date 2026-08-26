package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class CancelledReservationCannotBeConfirmedException extends ConflictException {

    public CancelledReservationCannotBeConfirmedException() {
        super("Cancelled reservation cannot be confirmed");
    }
}
