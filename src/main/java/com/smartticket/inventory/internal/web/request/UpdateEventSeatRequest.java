package com.smartticket.inventory.internal.web.request;

import com.smartticket.inventory.internal.domain.EventSeatStatus;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record UpdateEventSeatRequest(

        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal price,

        EventSeatStatus status
) {
}
