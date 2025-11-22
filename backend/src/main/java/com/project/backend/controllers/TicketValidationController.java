package com.project.backend.controllers;

import com.project.backend.domain.dtos.TicketValidationRequestDto;
import com.project.backend.domain.dtos.TicketValidationResponseDto;
import com.project.backend.domain.entities.TicketValidation;
import com.project.backend.domain.entities.TicketValidationMethod;
import com.project.backend.mappers.TicketValidationMapper;
import com.project.backend.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validationTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
            ) {
        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;
        if(TicketValidationMethod.MANUAL.equals( method )) {
            ticketValidation = ticketValidationService.validateTicketManually(
                    ticketValidationRequestDto.getId());
        } else {
            ticketValidation = ticketValidationService.validationTicketByQrCode(
                    ticketValidationRequestDto.getId()
            );
        }
        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResponseDto( ticketValidation ));
    }
}
