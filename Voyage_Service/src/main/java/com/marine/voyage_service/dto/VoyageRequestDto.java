package com.marine.voyage_service.dto;

import java.time.LocalDate;

public record VoyageRequestDto(String portOfLoading,
                               String portOfDischarging,
                               LocalDate startDate,
                               LocalDate endDate) {
}