package com.smartticket.event.internal.web.request;

import com.smartticket.event.internal.domain.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateFootballMatchRequest(

        @NotBlank(message = "Event name is required")
        @Size(max = 200)
        String name,

        @Size(max = 2000)
        String description,

        @NotNull(message = "Venue id is required")
        UUID venueId,

        @NotNull(message = "Start date/time is required")
        LocalDateTime startsAt,

        LocalDateTime endsAt,

        @NotNull(message = "Event status is required")
        EventStatus status,

        @NotNull(message = "Home team id is required")
        UUID homeTeamId,

        @NotNull(message = "Away team id is required")
        UUID awayTeamId,

        @NotNull(message = "Competition id is required")
        UUID competitionId
) {
}
