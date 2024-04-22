package com.example.crew_service.dto.certificate;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for responding with certificate information.
 */
public record CertificateResponseDto(long id, String name, LocalDate expireDate) {
}
