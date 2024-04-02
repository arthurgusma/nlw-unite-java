package com.gusmadev.passin.repositories;

import com.gusmadev.passin.domain.chechin.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckinRepository extends JpaRepository<CheckIn, Integer> {
}
