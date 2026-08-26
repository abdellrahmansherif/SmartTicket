package com.smartticket.reservation.internal.web.requests;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservationItemResponse(

        UUID id,
        UUID eventSeatId,
        BigDecimal price

) {
}
