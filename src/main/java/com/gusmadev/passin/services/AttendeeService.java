package com.gusmadev.passin.services;

import com.gusmadev.passin.domain.attendee.Attendee;
import com.gusmadev.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import com.gusmadev.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.gusmadev.passin.domain.checkIn.CheckIn;
import com.gusmadev.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.gusmadev.passin.dto.attendee.AttendeeDetails;
import com.gusmadev.passin.dto.attendee.AttendeesListResponseDTO;
import com.gusmadev.passin.dto.attendee.AttendeeBadgeDTO;
import com.gusmadev.passin.repositories.AttendeeRepository;
import com.gusmadev.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeResgistered =  this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeResgistered.isPresent()) throw new AttendeeAlreadyRegisteredException("Attendee is already registered!");
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID:" + attendeeId));
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        return new AttendeeBadgeResponseDTO(new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId()));
    }
}
