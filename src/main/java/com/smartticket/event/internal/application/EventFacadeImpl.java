package com.smartticket.event.internal.application;

import com.smartticket.event.api.EventFacade;
import com.smartticket.event.internal.persistence.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class EventFacadeImpl implements EventFacade {
    private final EventRepository eventRepository;
    @Override
    public boolean existsById(UUID eventId) {
        return eventRepository.existsById(eventId);
    }
}
