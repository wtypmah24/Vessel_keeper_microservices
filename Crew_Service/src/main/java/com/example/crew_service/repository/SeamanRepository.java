package com.example.crew_service.repository;

import com.example.crew_service.entity.seaman.Seaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SeamanRepository extends JpaRepository<Seaman, Long> {

    Optional<Seaman> findById(long id);
}
