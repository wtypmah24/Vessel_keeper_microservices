package com.example.crew_service.repository;

import com.example.crew_service.entity.seaman.RecordOfService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordOfServiceRepository extends JpaRepository<RecordOfService, Long> {
}
