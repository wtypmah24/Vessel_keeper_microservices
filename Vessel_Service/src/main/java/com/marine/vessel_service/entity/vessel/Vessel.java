package com.marine.vessel_service.entity.vessel;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * The Vessel class represents an entity for vessels in the marine service.
 * It contains information about the vessel's IMO number, name, vessel type, crew members, and assigned voyage ID.
 */
@Entity
@Table(name = "vessel")
@Data
@NoArgsConstructor
public class Vessel {
    @Id
    @Column(name = "imo_number", unique = true)
    private Long imoNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "vessel_type")
    @Enumerated(EnumType.STRING)
    private VesselType vesselType;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> crew = new HashSet<>();
    @Column(name = "voyageId")
    private Long voyageId;

    /**
     * Adds a crew member to the vessel's crew.
     *
     * @param id The ID of the crew member to be added.
     * @return {@code true} if the crew member was added successfully; {@code false} otherwise.
     */
    public Boolean addSeamanToCrew(Long id) {
        return crew.add(id);
    }

    /**
     * Removes a crew member from the vessel's crew.
     *
     * @param id The ID of the crew member to be removed.
     * @return {@code true} if the crew member was removed successfully; {@code false} otherwise.
     */
    public Boolean removeSeamanFromCrew(Long id) {
        return crew.remove(id);
    }

}