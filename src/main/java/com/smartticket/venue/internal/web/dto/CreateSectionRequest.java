package com.smartticket.venue.internal.web.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record CreateSectionRequest(

        @NotBlank(message = "Section name is required")
        @Size(max = 100)
        String name,

        @Size(max = 255)
        String description

) {
}