package com.project.backend.services.implementation;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.entities.Event;
import com.project.backend.domain.entities.TicketType;
import com.project.backend.domain.entities.User;
import com.project.backend.exceptions.UserNotFoundException;
import com.project.backend.repository.EventRepo;
import com.project.backend.repository.UserRepo;
import com.project.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepo userRepo;
    private final EventRepo eventRepo;


    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepo.findById( organizerId )
                .orElseThrow(() -> new UserNotFoundException(String.format( "User with id '%s' not found", organizerId )) );

       List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map( ticketType -> {
            TicketType ticketTypeToCreate = new TicketType();
            ticketTypeToCreate.setName( ticketType.getName() );
            ticketTypeToCreate.setPrice( ticketType.getPrice() );
            ticketTypeToCreate.setDescription( ticketType.getDescription() );
            ticketTypeToCreate.setTotalAvailable( ticketType.getTotalAvailable() );
            return ticketTypeToCreate;
        } ).toList();

        Event eventToCreate = new Event();
        eventToCreate.setName( event.getName() );
        eventToCreate.setStart( event.getStart() );
        eventToCreate.setEnd( event.getEnd() );
        eventToCreate.setVenue( event.getVenue() );
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd( event.getSalesEnd() );
        eventToCreate.setStatus( event.getStatus() );
        eventToCreate.setOrganizer( organizer );
        eventToCreate.setTicketTypes( ticketTypesToCreate );

        return eventRepo.save( eventToCreate );
    }
}
