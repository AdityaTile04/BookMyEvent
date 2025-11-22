package com.project.backend.mappers;

import com.project.backend.domain.dtos.GetTicketResponseDto;
import com.project.backend.domain.dtos.ListTicketResponseDto;
import com.project.backend.domain.dtos.ListTicketTicketTypeDtoResponse;
import com.project.backend.domain.entities.Ticket;
import com.project.backend.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    ListTicketTicketTypeDtoResponse toListTicketTicketTypeDtoResponse(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);

    @Mapping( target = "price", source = "ticket.ticketType.price")
    @Mapping( target = "description", source = "ticket.ticketType.description")
    @Mapping( target = "eventName", source = "ticket.ticketType.event.name")
    @Mapping( target = "eventVenue", source = "ticket.ticketType.event.venue")
    @Mapping( target = "eventStart", source = "ticket.ticketType.event.start")
    @Mapping( target = "eventEnd", source = "ticket.ticketType.event.end")
    GetTicketResponseDto toGetTicketResponseDto(Ticket ticket);

}
