package com.project.backend.controllers;

import com.project.backend.domain.dtos.ListPublishedEventResponseDto;
import com.project.backend.mappers.EventMapper;
import com.project.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/published-events")
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvent(Pageable pageable) {
        return ResponseEntity.ok(eventService.listPublishedEvents( pageable )
                .map( eventMapper::toListPublishedEventResponseDto ));
    }

}
