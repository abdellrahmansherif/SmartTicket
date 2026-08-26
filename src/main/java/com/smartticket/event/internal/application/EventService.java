package com.smartticket.event.internal.application;

import com.smartticket.event.internal.exception.EventNotFoundException;
import com.smartticket.event.internal.domain.Event;
import com.smartticket.event.internal.domain.EventStatus;
import com.smartticket.event.internal.persistence.EventRepository;
import com.smartticket.event.internal.web.response.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    public List<EventResponse> getAll(EventStatus status) {
        List<Event> events = status == null
                ? eventRepository.findAllByOrderByStartsAtAsc()
                : eventRepository.findByStatusOrderByStartsAtAsc(status);

        return events.stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse getById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new EventNotFoundException(id)
                );

        return toResponse(event);
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getVenueId(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getStatus()
        );
    }
}
