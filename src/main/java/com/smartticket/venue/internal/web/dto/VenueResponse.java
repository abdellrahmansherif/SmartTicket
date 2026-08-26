package com.smartticket.venue.internal.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record VenueResponse(
        UUID id,
        String name,
        String address,
        String city,
        String country,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
