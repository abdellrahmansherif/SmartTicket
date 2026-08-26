package com.smartticket.venue.internal.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateVenueRequest(

        @NotBlank(message = "Venue name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Address is required")
        @Size(max = 255)
        String address,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 100)
        String country,

        BigDecimal latitude,

        BigDecimal longitude

) {
}
