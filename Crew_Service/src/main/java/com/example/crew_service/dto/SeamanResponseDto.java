package com.example.crew_service.dto;

import java.util.Set;

public record SeamanResponseDto(long id, String name, String rank, Set<CertificateResponseDto> certificates, Set<SeamanServiceResponseDto> recordOfServices) {
}
