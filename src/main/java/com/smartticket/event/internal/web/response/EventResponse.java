package com.smartticket.event.internal.web.response;

import com.smartticket.event.internal.domain.EventStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String name,
        String description,
        UUID venueId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        EventStatus status
) {
}
