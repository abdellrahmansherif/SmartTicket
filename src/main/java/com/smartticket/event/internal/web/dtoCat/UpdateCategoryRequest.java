package com.smartticket.event.internal.web.dtoCat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateCategoryRequest(
        @NotBlank(message = "Category name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Category name must be between 2 and 100 characters"
        )
        String name,

        @NotNull(message = "Category image is required")
        MultipartFile image
) {
}
