package com.smartticket.event.api;

import java.util.UUID;

public interface EventFacade {
    boolean existsById(UUID eventId);
}
