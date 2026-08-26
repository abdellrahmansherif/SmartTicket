package com.smartticket.event.internal.web.response;

import java.util.UUID;

public record CompetitionResponse(
        UUID id,
        String name,
        String country,
        String logoUrl
) {
}
