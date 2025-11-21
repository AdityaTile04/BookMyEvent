package com.project.backend.controllers;

import com.project.backend.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.project.backend.domain.dtos.ListPublishedEventResponseDto;
import com.project.backend.domain.entities.Event;
import com.project.backend.mappers.EventMapper;
import com.project.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/published-events")
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvent(Pageable pageable, @RequestParam(required = false) String q) {

        Page<Event> events;
        if(null != q && !q.trim().isEmpty()) {
            events = eventService.searchPublishedEvents( q, pageable );
        } else {
            events = eventService.listPublishedEvents( pageable );
        }
        return ResponseEntity.ok(events.map( eventMapper::toListPublishedEventResponseDto ));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
            @PathVariable UUID eventId
            ) {
       return eventService.getPublishedEvent( eventId )
                .map( eventMapper::toGetPublishedEventDetailsResponseDto )
                .map( ResponseEntity::ok )
                .orElse( ResponseEntity.notFound().build() );
    }

}
