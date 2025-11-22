package com.project.backend.services;

import com.project.backend.domain.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {
    TicketValidation validationTicketByQrCode(UUID qrCodeId);
    TicketValidation validateTicketManually(UUID ticketId);

}
