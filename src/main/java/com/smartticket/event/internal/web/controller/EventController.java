package com.smartticket.event.internal.web.controller;

import com.smartticket.event.internal.application.EventService;
import com.smartticket.event.internal.domain.EventStatus;
import com.smartticket.event.internal.web.response.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getAllEvents(
            @RequestParam(required = false) EventStatus status
    ) {
        return eventService.getAll(status);
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable UUID id) {
        return eventService.getById(id);
    }
}
