package com.smartticket.venue.internal.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSeatRequest(

        @NotBlank(message = "Row label is required")
        @Size(max = 20)
        String rowLabel,

        @NotBlank(message = "Seat number is required")
        @Size(max = 20)
        String seatNumber

) {
}
