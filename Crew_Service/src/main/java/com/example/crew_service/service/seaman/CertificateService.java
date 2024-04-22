package com.example.crew_service.service.seaman;

import com.example.crew_service.dto.certificate.CertificateRequestDto;
import com.example.crew_service.dto.certificate.CertificateResponseDto;
import com.example.crew_service.entity.seaman.Seaman;
import com.example.crew_service.entity.seaman.SeamanCertificate;
import com.example.crew_service.exception.SeamanCertificateException;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.mapper.CertificateMapper;
import com.example.crew_service.repository.CertificateRepository;
import com.example.crew_service.repository.SeamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing seaman certificates.
 */
@Service
public class CertificateService {
    private final CertificateMapper certificateMapper;
    private final CertificateRepository certificateRepository;
    private final SeamanRepository seamanRepository;

    /**
     * Constructs a CertificateService with the given dependencies.
     *
     * @param certificateMapper     The mapper for mapping between DTOs and entities related to certificates
     * @param certificateRepository The repository for managing certificate entities
     * @param seamanRepository      The repository for managing seaman entities
     */
    @Autowired
    public CertificateService(CertificateMapper certificateMapper, CertificateRepository certificateRepository, SeamanRepository seamanRepository) {
        this.certificateMapper = certificateMapper;
        this.certificateRepository = certificateRepository;
        this.seamanRepository = seamanRepository;
    }

    /**
     * Adds a certificate to a seaman.
     *
     * @param candidate The certificate request DTO
     * @param seamanId  The ID of the seaman
     * @return A set of certificate response DTOs
     * @throws SeamanException            if the seaman with the provided ID is not found
     * @throws SeamanCertificateException if the provided certificate request DTO is invalid
     */
    @Transactional
    public Set<CertificateResponseDto> addCertificateToSeaman(CertificateRequestDto candidate, long seamanId) throws SeamanException, SeamanCertificateException {
        checkCertificateCandidate(candidate);
        Seaman seaman = seamanRepository.findById(seamanId).orElseThrow(() -> new SeamanException("There is no seaman with provided id: " + seamanId));
        return seaman.addCertificate(addCertificateToDb(candidate))
                .stream()
                .map(certificateMapper::certificateToCertificateResponseDto)
                .collect(Collectors.toSet());
    }

    /**
     * Deletes a certificate.
     *
     * @param id The ID of the certificate to delete
     * @return A set of certificate response DTOs
     * @throws SeamanCertificateException if the certificate with the provided ID is not found
     */
    @Transactional
    public Set<CertificateResponseDto> deleteCertificate(long id) throws SeamanCertificateException {
        SeamanCertificate certificate = certificateRepository.findById(id).orElseThrow(() -> new SeamanCertificateException("There is no certificate with id: " + id));
        Seaman seaman = certificate.getSeaman();
        removeCertificateFromDb(certificate);
        return seaman.removeCertificate(certificate)
                .stream()
                .map(certificateMapper::certificateToCertificateResponseDto)
                .collect(Collectors.toSet());
    }

    /**
     * Adds a certificate to the database.
     *
     * @param candidate The certificate request DTO
     * @return The saved seaman certificate entity
     */
    @Transactional
    public SeamanCertificate addCertificateToDb(CertificateRequestDto candidate) {
        return certificateRepository.save(certificateMapper.certificateRequestDtoToCertificate(candidate));
    }

    /**
     * Removes a certificate from the database.
     *
     * @param certificate The seaman certificate entity to remove
     */
    @Transactional
    public void removeCertificateFromDb(SeamanCertificate certificate) {
        certificateRepository.delete(certificate);
    }

    /**
     * Checks if the provided certificate candidate is valid.
     *
     * @param candidate The certificate request DTO to check
     * @throws SeamanCertificateException if the certificate candidate is invalid
     */
    private void checkCertificateCandidate(CertificateRequestDto candidate) throws SeamanCertificateException {
        if (candidate == null) throw new SeamanCertificateException("You didn't provide certificate to add.");
        if (candidate.name().isBlank()) throw new SeamanCertificateException("You didn't provide certificate's name.");
        if (candidate.expireDate().isBefore(LocalDate.now()))
            throw new SeamanCertificateException("Certificate is expired!");
    }
}
