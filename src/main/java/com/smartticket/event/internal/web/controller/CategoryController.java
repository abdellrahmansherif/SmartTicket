package com.smartticket.event.internal.web.controller;


import com.smartticket.event.internal.application.CategoryService;
import com.smartticket.event.internal.web.dtoCat.CategoryResponse;
import com.smartticket.event.internal.web.dtoCat.CreateCategoryRequest;
import com.smartticket.event.internal.web.dtoCat.UpdateCategoryRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    public CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(
            @Valid @ModelAttribute CreateCategoryRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.getAllActiveCategories());
    }

    // Update category
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public CategoryResponse updateCategory(
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdateCategoryRequest request
    ) {
        return categoryService.update(id, request);
    }
    // Disable category
    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateCategory(@PathVariable UUID id) {
        categoryService.deactivate(id);
    }

    // Enable category
    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateCategory(@PathVariable UUID id) {
        categoryService.activate(id);
    }
}
