package com.example.crew_service.repository;

import com.example.crew_service.entity.seaman.SeamanCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing seaman certificates.
 */
public interface CertificateRepository extends JpaRepository<SeamanCertificate, Long> {
    /**
     * Finds a seaman certificate by its ID.
     *
     * @param id The ID of the seaman certificate
     * @return An Optional containing the seaman certificate, or empty if not found
     */
    Optional<SeamanCertificate> findById(long id);
}