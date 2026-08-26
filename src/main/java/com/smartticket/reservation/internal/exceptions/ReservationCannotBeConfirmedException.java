package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;
import com.smartticket.reservation.internal.domain.ReservationStatus;

public class ReservationCannotBeConfirmedException extends ConflictException {

    public ReservationCannotBeConfirmedException(
            ReservationStatus status
    ) {
        super(
                "Reservation cannot be confirmed from status: "
                        + status
        );
    }
}
