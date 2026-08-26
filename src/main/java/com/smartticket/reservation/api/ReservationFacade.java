package com.smartticket.reservation.api;

import java.math.BigDecimal;
import java.util.UUID;


public interface ReservationFacade {
    void cancelReservation(UUID reservationId);

    ReservationDetails getReservation(UUID reservationId);

    void confirmReservation(UUID reservationId);
}
