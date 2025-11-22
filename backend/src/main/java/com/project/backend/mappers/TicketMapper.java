package com.project.backend.mappers;

import com.project.backend.domain.dtos.ListTicketResponseDto;
import com.project.backend.domain.dtos.ListTicketTicketTypeDtoResponse;
import com.project.backend.domain.entities.Ticket;
import com.project.backend.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    List<ListTicketTicketTypeDtoResponse> toListTicketTicketTypeDtoResponse(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);


}
