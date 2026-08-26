package com.smartticket.venue.internal.web.controller;


import com.smartticket.venue.internal.application.SeatService;
import com.smartticket.venue.internal.web.dto.CreateSeatRequest;
import com.smartticket.venue.internal.web.dto.SeatResponse;
import com.smartticket.venue.internal.web.dto.UpdateSeatRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;


    // Create seat inside section
    @PostMapping("/sections/{sectionId}/seats")
    @ResponseStatus(HttpStatus.CREATED)
    public SeatResponse createSeat(
            @PathVariable UUID sectionId,
            @Valid @RequestBody CreateSeatRequest request
    ) {

        return seatService.create(
                sectionId,
                request
        );
    }


    // Get all seats inside a section
    @GetMapping("/sections/{sectionId}/seats")
    public List<SeatResponse> getSeatsBySection(
            @PathVariable UUID sectionId
    ) {

        return seatService.getBySection(sectionId);
    }


    // Get specific seat
    @GetMapping("/seats/{id}")
    public SeatResponse getSeatById(
            @PathVariable UUID id
    ) {

        return seatService.getById(id);
    }


    // Update seat
    @PutMapping("/seats/{id}")
    public SeatResponse updateSeat(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSeatRequest request
    ) {

        return seatService.update(
                id,
                request
        );
    }


    // Delete seat
    @DeleteMapping("/seats/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeat(
            @PathVariable UUID id
    ) {

        seatService.delete(id);
    }
}