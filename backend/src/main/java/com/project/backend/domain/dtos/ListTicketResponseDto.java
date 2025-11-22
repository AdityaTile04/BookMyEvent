package com.project.backend.domain.dtos;

import com.project.backend.domain.entities.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ListTicketResponseDto {
    private UUID id;
    private TicketStatusEnum status;
    private ListTicketTicketTypeDtoResponse ticketType;
}
