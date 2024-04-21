package com.marine.vessel_service.repository;

import com.marine.vessel_service.entity.vessel.Vessel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The VesselRepository interface provides access to Vessel entities in the database.
 * It extends the JpaRepository interface, providing basic CRUD operations for Vessel entities.
 */
public interface VesselRepository extends JpaRepository<Vessel, Long> {
    /**
     * Finds a vessel by its IMO number.
     *
     * @param imoNumber The IMO number of the vessel.
     * @return An Optional containing the vessel if found, otherwise empty.
     */
    Optional<Vessel> findByImoNumber(long imoNumber);

    /**
     * Finds a vessel by its IMO number and with null voyage ID.
     *
     * @param imoNumber The IMO number of the vessel.
     * @return An Optional containing the vessel if found, otherwise empty.
     */
    Optional<Vessel> findByImoNumberAndVoyageIdNull(long imoNumber);

    /**
     * Finds vessels by their voyage ID.
     *
     * @param voyageId The ID of the voyage.
     * @return An Optional containing the vessel if found, otherwise empty.
     */
    Optional<Vessel> findVesselsByVoyageId(Long voyageId);
}
