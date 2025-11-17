package com.project.backend.services;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.entities.Event;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
