package com.smartticket.venue.internal.web.controller;


import com.smartticket.venue.internal.application.VenueService;
import com.smartticket.venue.internal.web.dto.CreateVenueRequest;
import com.smartticket.venue.internal.web.dto.UpdateVenueRequest;
import com.smartticket.venue.internal.web.dto.VenueResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/venues")
public class Controller {
    @Autowired
    public VenueService venueService;

    // Create venue
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenueResponse createVenue(
            @Valid @RequestBody CreateVenueRequest request
    ) {
        return venueService.create(request);
    }

    // Get all venues
    @GetMapping
    public List<VenueResponse> getAllVenues(
    ) {
        return venueService.getAll();
    }

    // Get venue by id
    @GetMapping("/{id}")
    public VenueResponse getVenueById(
            @PathVariable UUID id
    ) {
        return venueService.getById(id);
    }

    // Update venue
    @PutMapping("/{id}")
    public VenueResponse updateVenue(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVenueRequest request
    ) {
        return venueService.update(id, request);
    }

    // Delete venue
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVenue(
            @PathVariable UUID id
    ) {
        venueService.delete(id);
    }
}
