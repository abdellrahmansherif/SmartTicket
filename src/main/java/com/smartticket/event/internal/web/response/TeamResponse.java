package com.smartticket.event.internal.web.response;

import java.util.UUID;

public record TeamResponse(
        UUID id,
        String name,
        String shortName,
        String country,
        String logoUrl
) {
}
