package com.smartticket.inventory.internal.application;

import com.smartticket.inventory.internal.domain.EventSeat;
import com.smartticket.inventory.internal.domain.EventSeatStatus;
import com.smartticket.inventory.internal.exceptions.EventSeatAlreadyExistsException;
import com.smartticket.inventory.internal.exceptions.EventSeatNotFoundException;
import com.smartticket.inventory.internal.persistence.EventSeatRepository;
import com.smartticket.inventory.internal.web.request.CreateEventSeatRequest;
import com.smartticket.inventory.internal.web.request.UpdateEventSeatRequest;
import com.smartticket.inventory.internal.web.response.EventSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventSeatService {

    private final EventSeatRepository eventSeatRepository;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EventSeatResponse create(
            CreateEventSeatRequest request
    ) {

        boolean exists =
                eventSeatRepository
                        .existsByEventIdAndSeatId(
                                request.eventId(),
                                request.seatId()
                        );

        if (exists) {
            throw new EventSeatAlreadyExistsException();
        }

        EventSeat eventSeat = EventSeat.builder()
                .eventId(request.eventId())
                .seatId(request.seatId())
                .price(request.price())
                .status(
                        request.status() != null
                                ? request.status()
                                : EventSeatStatus.AVAILABLE
                )
                .build();

        EventSeat savedEventSeat =
                eventSeatRepository.save(eventSeat);

        return toResponse(savedEventSeat);
    }


    public EventSeatResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }


    public List<EventSeatResponse> getAll() {

        return eventSeatRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<EventSeatResponse> getByEventId(
            UUID eventId
    ) {

        return eventSeatRepository
                .findAllByEventId(eventId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<EventSeatResponse> getByEventIdAndStatus(
            UUID eventId,
            EventSeatStatus status
    ) {

        return eventSeatRepository
                .findAllByEventIdAndStatus(
                        eventId,
                        status
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public EventSeatResponse update(
            UUID id,
            UpdateEventSeatRequest request
    ) {

        EventSeat eventSeat = findEntity(id);

        if (request.price() != null) {
            eventSeat.setPrice(request.price());
        }

        if (request.status() != null) {
            eventSeat.setStatus(request.status());
        }

        return toResponse(eventSeat);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {

        EventSeat eventSeat = findEntity(id);

        eventSeatRepository.delete(eventSeat);
    }


    private EventSeat findEntity(UUID id) {

        return eventSeatRepository
                .findById(id)
                .orElseThrow(() ->
                        new EventSeatNotFoundException(id)
                );
    }


    private EventSeatResponse toResponse(
            EventSeat eventSeat
    ) {

        return new EventSeatResponse(
                eventSeat.getId(),
                eventSeat.getEventId(),
                eventSeat.getSeatId(),
                eventSeat.getPrice(),
                eventSeat.getStatus()
        );
    }
}