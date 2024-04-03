package com.gusmadev.passin.services;

import com.gusmadev.passin.domain.attendee.Attendee;
import com.gusmadev.passin.domain.event.Event;
import com.gusmadev.passin.dto.event.EventResponseDTO;
import com.gusmadev.passin.repositories.AttendeeRepository;
import com.gusmadev.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSerivce {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with Id:" + eventId));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }
}
