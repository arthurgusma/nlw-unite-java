package com.gusmadev.passin.controllers;

import com.gusmadev.passin.domain.attendee.Attendee;
import com.gusmadev.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.gusmadev.passin.dto.attendee.AttendeeIdDTO;
import com.gusmadev.passin.dto.attendee.AttendeeRequestDTO;
import com.gusmadev.passin.dto.attendee.AttendeesListResponseDTO;
import com.gusmadev.passin.dto.event.EventIdDTO;
import com.gusmadev.passin.dto.event.EventRequestDTO;
import com.gusmadev.passin.dto.event.EventResponseDTO;
import com.gusmadev.passin.services.AttendeeService;
import com.gusmadev.passin.services.EventSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventSerivce eventSerivce;
    private final AttendeeService attendeeService;
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        EventResponseDTO event = this.eventSerivce.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }
    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventId = this.eventSerivce.createEvent(body);
        var uri = uriComponentsBuilder.path("/events/{eventId}").buildAndExpand(eventId.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventId);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendee(@PathVariable String id){
        AttendeesListResponseDTO attendeeListResponse = this.attendeeService.getEventsAttendee(id);
        return ResponseEntity.ok(attendeeListResponse);
    }

    @PostMapping("/{eventId}/badge")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(
            @PathVariable String eventId,
            @RequestBody AttendeeRequestDTO body,
            UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventSerivce.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

}
