package com.smartticket.inventory.internal.web.controller;

import com.smartticket.inventory.internal.application.EventSeatService;
import com.smartticket.inventory.internal.domain.EventSeatStatus;
import com.smartticket.inventory.internal.web.request.CreateEventSeatRequest;
import com.smartticket.inventory.internal.web.request.UpdateEventSeatRequest;
import com.smartticket.inventory.internal.web.response.EventSeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-seats")
@RequiredArgsConstructor
public class EventSeatController {

    private final EventSeatService eventSeatService;

    @PostMapping
    public ResponseEntity<EventSeatResponse> create(
            @Valid @RequestBody CreateEventSeatRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventSeatService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSeatResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventSeatService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventSeatResponse>> getAll(
            @RequestParam(required = false) UUID eventId,
            @RequestParam(required = false) EventSeatStatus status
    ) {
        if (eventId != null && status != null) {
            return ResponseEntity.ok(
                    eventSeatService.getByEventIdAndStatus(eventId, status)
            );
        }

        if (eventId != null) {
            return ResponseEntity.ok(eventSeatService.getByEventId(eventId));
        }

        return ResponseEntity.ok(eventSeatService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventSeatResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEventSeatRequest request
    ) {
        return ResponseEntity.ok(eventSeatService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        eventSeatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
