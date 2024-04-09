package com.gusmadev.passin.config;

import com.gusmadev.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import com.gusmadev.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.gusmadev.passin.domain.checkIn.exceptions.CheckInAlreadyExistsException;
import com.gusmadev.passin.domain.event.exceptions.EventFullExcepetion;
import com.gusmadev.passin.domain.event.exceptions.EventNotFoundException;
import com.gusmadev.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyRegisteredException.class)
    public ResponseEntity handleAttendeeAlreadyExists(AttendeeNotFoundException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ResponseEntity handleCheckInAlreadyExists(CheckInAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventFullExcepetion.class)
    public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullExcepetion exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }
}
