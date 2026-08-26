package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;
import com.smartticket.reservation.internal.domain.ReservationStatus;

public class ReservationCannotBeCancelledException extends ConflictException {

    public ReservationCannotBeCancelledException(
            ReservationStatus status
    ) {
        super(
                "Reservation cannot be cancelled from status: "
                        + status
        );
    }
}
