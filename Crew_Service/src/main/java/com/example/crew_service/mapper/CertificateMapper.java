package com.example.crew_service.mapper;

import com.example.crew_service.dto.certificate.CertificateRequestDto;
import com.example.crew_service.dto.certificate.CertificateResponseDto;
import com.example.crew_service.entity.seaman.SeamanCertificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for mapping between certificate DTOs and entities.
 */
@Mapper(componentModel = "spring")
public interface CertificateMapper {
    /**
     * Maps a certificate request DTO to a certificate entity.
     *
     * @param certificateCandidate The certificate request DTO
     * @return The mapped certificate entity
     */
    @Mapping(target = "seaman", ignore = true)
    SeamanCertificate certificateRequestDtoToCertificate(CertificateRequestDto certificateCandidate);

    /**
     * Maps a certificate entity to a certificate response DTO.
     *
     * @param certificate The certificate entity
     * @return The mapped certificate response DTO
     */
    CertificateResponseDto certificateToCertificateResponseDto(SeamanCertificate certificate);
}