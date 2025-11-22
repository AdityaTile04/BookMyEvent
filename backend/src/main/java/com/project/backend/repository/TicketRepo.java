package com.project.backend.repository;

import com.project.backend.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, UUID> {
    int countByTicketTypeId(UUID ticketTypeId);
}
