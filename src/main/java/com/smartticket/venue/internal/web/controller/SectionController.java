package com.smartticket.venue.internal.web.controller;
import com.smartticket.venue.internal.application.SectionService;
import com.smartticket.venue.internal.web.dto.CreateSectionRequest;
import com.smartticket.venue.internal.web.dto.SectionResponse;
import com.smartticket.venue.internal.web.dto.UpdateSectionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    // Create section inside venue
    @PostMapping("/venues/{venueId}/sections")
    @ResponseStatus(HttpStatus.CREATED)
    public SectionResponse createSection(
            @PathVariable UUID venueId,
            @Valid @RequestBody CreateSectionRequest request
    ) {
        return sectionService.create(venueId, request);
    }

    // Get all sections of a venue
    @GetMapping("/venues/{venueId}/sections")
    public List<SectionResponse> getSectionsByVenue(
            @PathVariable UUID venueId
    ) {
        return sectionService.getByVenue(venueId);
    }

    // Get section by id
    @GetMapping("/sections/{id}")
    public SectionResponse getSectionById(
            @PathVariable UUID id
    ) {
        return sectionService.getById(id);
    }

    // Update section
    @PutMapping("/sections/{id}")
    public SectionResponse updateSection(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSectionRequest request
    ) {
        return sectionService.update(id, request);
    }

    // Delete section
    @DeleteMapping("/sections/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(
            @PathVariable UUID id
    ) {
        sectionService.delete(id);
    }
}
