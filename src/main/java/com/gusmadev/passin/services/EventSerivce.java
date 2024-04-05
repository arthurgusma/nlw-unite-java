package com.gusmadev.passin.services;

import com.gusmadev.passin.domain.attendee.Attendee;
import com.gusmadev.passin.domain.event.Event;
import com.gusmadev.passin.domain.event.exceptions.EventFullExcepetion;
import com.gusmadev.passin.domain.event.exceptions.EventNotFoundException;
import com.gusmadev.passin.dto.attendee.AttendeeIdDTO;
import com.gusmadev.passin.dto.attendee.AttendeeRequestDTO;
import com.gusmadev.passin.dto.event.EventIdDTO;
import com.gusmadev.passin.dto.event.EventRequestDTO;
import com.gusmadev.passin.dto.event.EventResponseDTO;
import com.gusmadev.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSerivce {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.getAttendeeList(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }


    public EventIdDTO createEvent(EventRequestDTO eventRequestDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDTO.title());
        newEvent.setDetails(eventRequestDTO.details());
        newEvent.setMaximumAttendees(eventRequestDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventRequestDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequest){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequest.email(), eventId);
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.getAttendeeList(eventId);

        if (event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullExcepetion("Event is full!");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequest.name());
        newAttendee.setEmail(attendeeRequest.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id:" + eventId));
    }

    private List<Attendee> getAttendeeList(String eventId){
        return this.attendeeService.getAllAttendeesFromEvent(eventId);
    }
    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("[\\s+]", "-")
                .toLowerCase();
    }
}
