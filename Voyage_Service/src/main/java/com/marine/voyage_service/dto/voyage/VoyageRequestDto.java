package com.marine.voyage_service.dto.voyage;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a voyage request.
 */

public record VoyageRequestDto(String portOfLoading,
                               String portOfDischarging,
                               LocalDate startDate,
                               LocalDate endDate) {
}