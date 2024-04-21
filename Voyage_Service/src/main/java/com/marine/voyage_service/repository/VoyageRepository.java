package com.marine.voyage_service.repository;

import com.marine.voyage_service.entity.voyage.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface VoyageRepository extends JpaRepository<Voyage, Long> {

    Optional<Voyage> findById(long id);

    List<Voyage> findByIsVacantIsTrue();

    List<Voyage> findByPortOfDischargingAndEndDateAndIsVacantIsTrue(String portOfDischarging, LocalDate endDate);
    Optional<Voyage> findByPortOfDischargingAndEndDateAndIsVacantIsFalse(String portOfDischarging, LocalDate endDate);

    List<Voyage> findByPortOfLoadingAndStartDateAndIsVacantIsTrue(String portOfLoading, LocalDate startDate);
}
