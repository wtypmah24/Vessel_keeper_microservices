package com.marine.voyage_service.repository;

import com.marine.voyage_service.entity.voyage.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for interacting with Voyage entities.
 */
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    /**
     * Finds all vacant voyages.
     *
     * @return List of vacant voyages
     */
    List<Voyage> findByIsVacantIsTrue();

    /**
     * Finds vacant voyages by port of discharging, end date, and vacancy status.
     *
     * @param portOfDischarging The port of discharging
     * @param endDate           The end date
     * @return List of vacant voyages matching the criteria
     */
    List<Voyage> findByPortOfDischargingAndEndDateAndIsVacantIsTrue(String portOfDischarging, LocalDate endDate);

    /**
     * Finds a non-vacant voyage by port of discharging, end date, and vacancy status.
     *
     * @param portOfDischarging The port of discharging
     * @param endDate           The end date
     * @return Optional containing the non-vacant voyage if found, empty otherwise
     */
    Optional<Voyage> findByPortOfDischargingAndEndDateAndIsVacantIsFalse(String portOfDischarging, LocalDate endDate);

    /**
     * Finds vacant voyages by port of loading, start date, and vacancy status.
     *
     * @param portOfLoading The port of loading
     * @param startDate     The start date
     * @return List of vacant voyages matching the criteria
     */
    List<Voyage> findByPortOfLoadingAndStartDateAndIsVacantIsTrue(String portOfLoading, LocalDate startDate);
}
