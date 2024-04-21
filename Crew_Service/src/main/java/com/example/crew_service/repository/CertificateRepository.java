package com.example.crew_service.repository;

import com.example.crew_service.entity.seaman.SeamanCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CertificateRepository extends JpaRepository<SeamanCertificate, Long> {

    Optional<SeamanCertificate> findById(long id);
}
