package com.project.backend.mappers;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.CreateTicketTypeRequest;
import com.project.backend.domain.UpdateEventRequest;
import com.project.backend.domain.UpdateTicketTypeRequest;
import com.project.backend.domain.dtos.*;
import com.project.backend.domain.entities.Event;
import com.project.backend.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeResponseDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

    UpdateEventResponseDto toDtoUpdateEventResponseDto(Event event);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

}
