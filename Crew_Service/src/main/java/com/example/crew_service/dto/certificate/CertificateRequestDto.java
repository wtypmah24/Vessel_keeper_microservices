package com.example.crew_service.dto.certificate;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for requesting a certificate.
 */
public record CertificateRequestDto(String name, LocalDate expireDate) {
}
