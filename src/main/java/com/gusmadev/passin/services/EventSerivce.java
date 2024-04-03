package com.gusmadev.passin.services;

import com.gusmadev.passin.domain.attendee.Attendee;
import com.gusmadev.passin.domain.event.Event;
import com.gusmadev.passin.domain.event.exceptions.EventNotFoundException;
import com.gusmadev.passin.dto.event.EventIdDTO;
import com.gusmadev.passin.dto.event.EventRequestDTO;
import com.gusmadev.passin.dto.event.EventResponseDTO;
import com.gusmadev.passin.repositories.AttendeeRepository;
import com.gusmadev.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSerivce {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id:" + eventId));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
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

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("[\\s+]", "-")
                .toLowerCase();
    }
}
