package com.smartticket.event.internal.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCompetitionRequest(

        @NotBlank(message = "Competition name is required")
        @Size(max = 150)
        String name,

        @Size(max = 100)
        String country,

        @Size(max = 500)
        String logoUrl
) {
}
