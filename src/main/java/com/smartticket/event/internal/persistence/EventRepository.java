package com.smartticket.event.internal.persistence;

import com.smartticket.event.internal.domain.Event;
import com.smartticket.event.internal.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByOrderByStartsAtAsc();

    List<Event> findByStatusOrderByStartsAtAsc(EventStatus status);
}
