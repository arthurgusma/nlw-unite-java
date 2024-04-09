package com.gusmadev.passin.repositories;

import com.gusmadev.passin.domain.checkIn.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<CheckIn, Integer> {

    Optional<CheckIn> findByAttendeeId(String attendeeId);
}
