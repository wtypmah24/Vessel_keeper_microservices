package com.marine.voyage_service.entity.voyage;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity class representing a voyage.
 */
@Entity
@Table(name = "voyage")
@NoArgsConstructor
@Data
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "port_of_loading")
    private String portOfLoading;
    @Column(name = "port_of_discharging")
    private String portOfDischarging;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "is_vacant")
    private boolean isVacant = true;
}