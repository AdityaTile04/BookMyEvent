package com.project.backend.services.implementation;

import com.project.backend.domain.entities.Ticket;
import com.project.backend.repository.TicketRepo;
import com.project.backend.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;

    @Override
    public Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable) {
       return ticketRepo.findByPurchaserId( userId, pageable );
    }
}
