package com.project.backend.services;

import com.project.backend.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketService {

    Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable);


}
