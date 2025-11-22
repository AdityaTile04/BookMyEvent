package com.project.backend.services;

import com.project.backend.domain.entities.QrCode;
import com.project.backend.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);

}
