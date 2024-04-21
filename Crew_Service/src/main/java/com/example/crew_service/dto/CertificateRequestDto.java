package com.example.crew_service.dto;

import java.time.LocalDate;

public record CertificateRequestDto(String name, LocalDate expireDate) {
}
