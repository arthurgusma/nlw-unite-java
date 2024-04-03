package com.gusmadev.passin.controllers;

import com.gusmadev.passin.services.EventSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventCrontroller {

    private final EventSerivce service;
    @GetMapping("/{eventId}")
    public ResponseEntity<String> getEvent(@PathVariable String eventId){
        this.service.getEventDetail(eventId);
        return ResponseEntity.ok("Success");
    }
}
