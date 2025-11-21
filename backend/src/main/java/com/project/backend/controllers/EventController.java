package com.project.backend.controllers;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.dtos.CreateEventRequestDto;
import com.project.backend.domain.dtos.CreateEventResponseDto;
import com.project.backend.domain.dtos.ListEventResponseDto;
import com.project.backend.domain.entities.Event;
import com.project.backend.mappers.EventMapper;
import com.project.backend.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
           @Valid @RequestBody CreateEventRequestDto createEventRequestDto) {
                CreateEventRequest createEventRequest = eventMapper.fromDto( createEventRequestDto);
        UUID userId = parseUserId( jwt );
        Event createEvent = eventService.createEvent( userId,  createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto( createEvent );
        return new ResponseEntity<>( createEventResponseDto, HttpStatus.CREATED );
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
       UUID userId = parseUserId( jwt );
        Page<Event> events = eventService.listEventsForOrganizer(userId,  pageable );
       return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString( jwt.getSubject() );
    }

}
