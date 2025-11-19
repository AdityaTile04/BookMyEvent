package com.project.backend.mappers;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.CreateTicketTypeRequest;
import com.project.backend.domain.dtos.CreateEventRequestDto;
import com.project.backend.domain.dtos.CreateEventResponseDto;
import com.project.backend.domain.dtos.CreateTicketTypeResponseDto;
import com.project.backend.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeResponseDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

}
