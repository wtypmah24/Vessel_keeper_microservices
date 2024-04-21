package com.example.crew_service.service.seaman;

import com.example.crew_service.dto.CertificateRequestDto;
import com.example.crew_service.dto.CertificateResponseDto;
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


@Service
public class CertificateService {
    private final CertificateMapper certificateMapper;
    private final CertificateRepository certificateRepository;
    private final SeamanRepository seamanRepository;


    @Autowired
    public CertificateService(CertificateMapper certificateMapper, CertificateRepository certificateRepository, SeamanRepository seamanRepository) {
        this.certificateMapper = certificateMapper;
        this.certificateRepository = certificateRepository;
        this.seamanRepository = seamanRepository;
    }


    @Transactional
    public Set<CertificateResponseDto> addCertificateToSeaman(CertificateRequestDto candidate, long seamanId) throws SeamanException, SeamanCertificateException {
        checkCertificateCandidate(candidate);
        Seaman seaman = seamanRepository.findById(seamanId).orElseThrow(() -> new SeamanException("There is no seaman with provided id: " + seamanId));
        return seaman.addCertificate(addCertificateToDb(candidate))
                .stream()
                .map(certificateMapper::certificateToCertificateResponseDto)
                .collect(Collectors.toSet());
    }


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
    @Transactional
    private SeamanCertificate addCertificateToDb(CertificateRequestDto candidate) {
        return certificateRepository.save(certificateMapper.certificateRequestDtoToCertificate(candidate));
    }

    private void removeCertificateFromDb(SeamanCertificate certificate) {
        certificateRepository.delete(certificate);
    }

    private void checkCertificateCandidate(CertificateRequestDto candidate) throws SeamanCertificateException {
        if (candidate == null) throw new SeamanCertificateException("You didn't provide certificate to add.");
        if (candidate.name().isBlank()) throw new SeamanCertificateException("You didn't provide certificate's name.");
        if (candidate.expireDate().isBefore(LocalDate.now()))
            throw new SeamanCertificateException("Certificate is expired!");
    }
}
