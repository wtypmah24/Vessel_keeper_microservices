package com.example.crew_service.dto.seaman;

import com.example.crew_service.dto.certificate.CertificateResponseDto;
import com.example.crew_service.dto.record.SeamanServiceResponseDto;

import java.util.Set;

/**
 * Data transfer object (DTO) representing seaman response information.
 */
public record SeamanResponseDto(long id, String name, String rank, Set<CertificateResponseDto> certificates,
                                Set<SeamanServiceResponseDto> recordOfServices) {
}
