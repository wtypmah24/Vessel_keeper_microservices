package com.example.crew_service.entity.seaman;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a seaman.
 */
@Entity
@Table(name = "seaman")
@Data
@EqualsAndHashCode(exclude = {"recordOfServices", "certificates", "hasJob"})
@ToString(exclude = {"recordOfServices", "certificates"})
@NoArgsConstructor
public class Seaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "rank")
    @Enumerated(EnumType.STRING)
    private Rank rank;
    @Column(name = "has_job")
    private boolean hasJob = false;
    @OneToMany(mappedBy = "seaman", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SeamanCertificate> certificates = new HashSet<>();
    @OneToMany(mappedBy = "seaman", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecordOfService> recordOfServices = new HashSet<>();

    /**
     * Adds a certificate to the seaman's certificates.
     *
     * @param certificate The certificate to add
     * @return The updated set of certificates
     */
    public Set<SeamanCertificate> addCertificate(SeamanCertificate certificate) {
        this.certificates.add(certificate);
        certificate.setSeaman(this);
        return certificates;
    }

    /**
     * Removes a certificate from the seaman's certificates.
     *
     * @param certificate The certificate to remove
     * @return The updated set of certificates
     */
    public Set<SeamanCertificate> removeCertificate(SeamanCertificate certificate) {
        this.certificates.remove(certificate);
        return certificates;
    }

    /**
     * Adds a record of service to the seaman's record of services.
     *
     * @param record The record of service to add
     */

    public void addServiceRecord(RecordOfService record) {
        this.recordOfServices.add(record);
        record.setSeaman(this);
    }
}