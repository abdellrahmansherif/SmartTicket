package com.smartticket.event.internal.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTeamRequest(

        @NotBlank(message = "Team name is required")
        @Size(max = 150)
        String name,

        @Size(max = 20)
        String shortName,

        @Size(max = 100)
        String country,

        @Size(max = 500)
        String logoUrl
) {
}
