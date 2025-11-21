package com.project.backend.services.implementation;

import com.project.backend.domain.CreateEventRequest;
import com.project.backend.domain.UpdateEventRequest;
import com.project.backend.domain.UpdateTicketTypeRequest;
import com.project.backend.domain.entities.Event;
import com.project.backend.domain.entities.EventStatusEnum;
import com.project.backend.domain.entities.TicketType;
import com.project.backend.domain.entities.User;
import com.project.backend.exceptions.EventNotFoundException;
import com.project.backend.exceptions.EventUpdateException;
import com.project.backend.exceptions.TicketTypeNotFoundException;
import com.project.backend.exceptions.UserNotFoundException;
import com.project.backend.repository.EventRepo;
import com.project.backend.repository.UserRepo;
import com.project.backend.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepo userRepo;
    private final EventRepo eventRepo;


    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepo.findById( organizerId )
                .orElseThrow(() -> new UserNotFoundException(String.format( "User with id '%s' not found", organizerId )) );

        Event eventToCreate = new Event();

       List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map( ticketType -> {
            TicketType ticketTypeToCreate = new TicketType();
            ticketTypeToCreate.setName( ticketType.getName() );
            ticketTypeToCreate.setPrice( ticketType.getPrice() );
            ticketTypeToCreate.setDescription( ticketType.getDescription() );
            ticketTypeToCreate.setTotalAvailable( ticketType.getTotalAvailable() );
            ticketTypeToCreate.setEvent( eventToCreate );
            return ticketTypeToCreate;
        } ).toList();

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

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
      return eventRepo.findByOrganizerId( organizerId, pageable );
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepo.findByIdAndOrganizerId( id, organizerId );
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {
        if(null == event.getId()) {
            throw new EventUpdateException("Event ID cannot be null");
        }
        if(!id.equals( event.getId() )) {
            throw new EventUpdateException("Cannot update the ID of an event");
        }

       Event existingEvent = eventRepo.
                findByIdAndOrganizerId( id, organizerId )
                .orElseThrow(() -> new EventNotFoundException(String.format( "Event With ID '%s' does not exists ", id )) );

        existingEvent.setName( event.getName() );
        existingEvent.setStart( event.getStart() );
        existingEvent.setEnd( event.getEnd() );
        existingEvent.setVenue( event.getVenue() );
        existingEvent.setSalesStart( event.getSalesStart() );
        existingEvent.setSalesEnd( event.getSalesEnd() );
        existingEvent.setStatus( event.getStatus() );

       Set<UUID> requestTicketTypeIds = event.getTicketTypes()
                .stream()
                .map( UpdateTicketTypeRequest::getId )
                .filter( Objects::nonNull )
                .collect( Collectors.toSet() );

       existingEvent.getTicketTypes().removeIf( existingTicketType ->
               !requestTicketTypeIds.contains( existingTicketType.getId() ));

       Map<UUID, TicketType> existingTicketTypeIndex = existingEvent.getTicketTypes().stream()
               .collect(Collectors.toMap( TicketType::getId,  Function.identity())  );

       for(UpdateTicketTypeRequest ticketType: event.getTicketTypes()) {
           if(null == ticketType.getId()) {
               TicketType ticketTypeToCreate = new TicketType();
               ticketTypeToCreate.setName( ticketType.getName() );
               ticketTypeToCreate.setPrice( ticketType.getPrice() );
               ticketTypeToCreate.setDescription( ticketType.getDescription() );
               ticketTypeToCreate.setTotalAvailable( ticketType.getTotalAvailable() );
               ticketTypeToCreate.setEvent( existingEvent );
               existingEvent.getTicketTypes().add( ticketTypeToCreate );
           } else if(existingTicketTypeIndex.containsKey( ticketType.getId() )) {
                TicketType existingTicketType = existingTicketTypeIndex.get(ticketType.getId());
                existingTicketType.setName( ticketType.getName() );
                existingTicketType.setPrice( ticketType.getPrice() );
                existingTicketType.setDescription( ticketType.getDescription() );
                existingTicketType.setTotalAvailable( ticketType.getTotalAvailable() );
           } else {
               throw new TicketTypeNotFoundException(String.format(
                       "Ticket type with ID '%s' does not exists", ticketType.getId()
               ));
           }
       }
           return eventRepo.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID id) {
        getEventForOrganizer( organizerId, id ).ifPresent( eventRepo::delete );
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
        return eventRepo.findByStatus( EventStatusEnum.PUBLISHED, pageable );
    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        return eventRepo.searchEvents( query, pageable );
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID id) {
        return eventRepo.findByIdAndStatus( id, EventStatusEnum.PUBLISHED );
    }
}
