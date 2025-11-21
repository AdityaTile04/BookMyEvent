package com.project.backend.services;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.UpdateEventRequest;
import com.project.backend.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);
    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event);
    void deleteEventForOrganizer(UUID organizerId, UUID id);
    Page<Event> listPublishedEvents(Pageable pageable);
}
