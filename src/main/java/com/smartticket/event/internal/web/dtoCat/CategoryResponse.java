package com.smartticket.event.internal.web.dtoCat;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String imageUrl
) {
}
