package com.smartticket.inventory.internal.web.request;

import com.smartticket.inventory.internal.domain.EventSeatStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateEventSeatRequest(

        @NotNull
        UUID eventId,

        @NotNull
        UUID seatId,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal price,

        EventSeatStatus status
) {
}
