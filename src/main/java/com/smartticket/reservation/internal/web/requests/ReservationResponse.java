package com.smartticket.reservation.internal.web.requests;

import com.smartticket.reservation.internal.domain.ReservationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID eventId,
        ReservationStatus status,
        BigDecimal totalAmount,
        Instant expiresAt,
        List<ReservationItemResponse> items
) {
}
