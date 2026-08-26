package com.smartticket.reservation.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReservationDetails(
        UUID id,
        UUID userId,
        BigDecimal totalAmount,
        String status,
        Instant expiresAt
) {
}
