package com.smartticket.venue.internal.web.dto;

import java.util.UUID;

public record SeatResponse(

        UUID id,
        String rowLabel,
        String seatNumber,
        UUID sectionId

) {
}