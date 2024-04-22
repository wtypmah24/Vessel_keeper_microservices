package com.example.crew_service.repository;

import com.example.crew_service.entity.seaman.Seaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing seamen.
 */
public interface SeamanRepository extends JpaRepository<Seaman, Long> {
    /**
     * Finds a seaman by ID.
     *
     * @param id The ID of the seaman to find.
     * @return An Optional containing the seaman if found, otherwise empty.
     */
    Optional<Seaman> findById(long id);
}
