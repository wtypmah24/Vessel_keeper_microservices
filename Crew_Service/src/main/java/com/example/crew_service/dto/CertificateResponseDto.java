package com.example.crew_service.dto;

import java.time.LocalDate;

public record CertificateResponseDto(long id, String name, LocalDate expireDate) {
}
