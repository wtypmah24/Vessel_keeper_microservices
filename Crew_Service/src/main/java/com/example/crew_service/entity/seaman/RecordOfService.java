package com.example.crew_service.entity.seaman;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing a record of service.
 */
@Entity
@Table(name = "record_of_service")
@Data
@EqualsAndHashCode(exclude = {"seaman"})
@ToString(exclude = {"seaman"})
@NoArgsConstructor
public class RecordOfService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seaman_id")
    private Seaman seaman;
    @Column(name = "vessel_imo_number")
    private Long imoNumber;
    @Column(name = "comment")
    private String comment;
}