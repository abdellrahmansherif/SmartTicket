package com.smartticket.inventory.api;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservableEventSeat(

        UUID eventSeatId,
        UUID eventId,
        BigDecimal price

) {
}
