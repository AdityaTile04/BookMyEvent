package com.project.backend.services.implementation;

import com.project.backend.domain.entities.Ticket;
import com.project.backend.domain.entities.TicketStatusEnum;
import com.project.backend.domain.entities.TicketType;
import com.project.backend.domain.entities.User;
import com.project.backend.exceptions.TicketSoldOutException;
import com.project.backend.exceptions.TicketTypeNotFoundException;
import com.project.backend.exceptions.UserNotFoundException;
import com.project.backend.repository.TicketRepo;
import com.project.backend.repository.TicketTypeRepo;
import com.project.backend.repository.UserRepo;
import com.project.backend.services.QrCodeService;
import com.project.backend.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepo userRepo;
    private final TicketTypeRepo ticketTypeRepo;
    private final TicketRepo ticketRepo;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepo.findById( userId ).orElseThrow( () -> new UserNotFoundException( String.format( "User with ID %s was not found", userId ) ) );
        TicketType ticketType = ticketTypeRepo.findByIdWithLock( ticketTypeId ).orElseThrow( () -> new TicketTypeNotFoundException(
                String.format( "Ticket type with ID %s not found", ticketTypeId )
        ) );
        int purchaseTickets = ticketRepo.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchaseTickets + 1 > totalAvailable) {
            throw new TicketSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus( TicketStatusEnum.PURCHASED );
        ticket.setTicketType( ticketType );
        ticket.setPurchaser( user );

        Ticket savedTicket = ticketRepo.save( ticket );
        qrCodeService.generateQrCode( savedTicket );

         return ticketRepo.save( savedTicket );
    }
}
