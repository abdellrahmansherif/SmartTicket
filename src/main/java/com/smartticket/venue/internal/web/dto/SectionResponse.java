package com.smartticket.venue.internal.web.dto;

import java.util.UUID;
public record SectionResponse(

        UUID id,
        String name,
        String description,
        UUID venueId

) {
}
