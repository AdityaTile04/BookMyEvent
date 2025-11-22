package com.project.backend.services.implementation;

import com.project.backend.domain.entities.*;
import com.project.backend.exceptions.QrCodeNotFoundException;
import com.project.backend.exceptions.TicketNotFoundException;
import com.project.backend.repository.QrCodeRepo;
import com.project.backend.repository.TicketRepo;
import com.project.backend.repository.TicketValidationRepo;
import com.project.backend.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepo qrCodeRepo;
    private final TicketValidationRepo ticketValidationRepo;
    private TicketRepo ticketRepo;

    @Override
    public TicketValidation validationTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepo.findByIdAndStatus( qrCodeId, QrCodeStatusEnum.ACTIVE )
                .orElseThrow( () -> new QrCodeNotFoundException(
                        String.format(
                                "QR Code with ID %s not found", qrCodeId
                        )
                ) );

        Ticket ticket = qrCode.getTicket();

        return validateTicket( ticket, TicketValidationMethod.QR_SCAN );

    }

    private TicketValidation validateTicket(Ticket ticket, TicketValidationMethod ticketValidationMethod) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket( ticket );
        ticketValidation.setValidationMethod( ticketValidationMethod );

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter( v -> TicketValidationStatusEnum.VALID.equals( v.getStatus() ) )
                .findFirst()
                .map( v -> TicketValidationStatusEnum.INVALID )
                .orElse( TicketValidationStatusEnum.VALID );


        ticketValidation.setStatus( ticketValidationStatus );

        return ticketValidationRepo.save( ticketValidation );
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepo.findById( ticketId ).orElseThrow(TicketNotFoundException::new );
        return validateTicket( ticket, TicketValidationMethod.MANUAL );
    }
}
