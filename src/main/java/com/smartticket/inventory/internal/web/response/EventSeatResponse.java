package com.smartticket.inventory.internal.web.response;

import com.smartticket.inventory.internal.domain.EventSeatStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record EventSeatResponse(
        UUID id,
        UUID eventId,
        UUID seatId,
        BigDecimal price,
        EventSeatStatus status
) {
}
