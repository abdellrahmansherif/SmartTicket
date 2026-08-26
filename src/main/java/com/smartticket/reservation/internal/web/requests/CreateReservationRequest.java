package com.smartticket.reservation.internal.web.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateReservationRequest(

        @NotNull(message = "Event ID is required")
        UUID eventId,

        @NotEmpty(message = "At least one event seat must be selected")
        @Size(max = 10, message = "You cannot reserve more than 10 seats")
        List<
                @NotNull(message = "Event seat ID cannot be null")
                        UUID
                > eventSeatIds

) {
}