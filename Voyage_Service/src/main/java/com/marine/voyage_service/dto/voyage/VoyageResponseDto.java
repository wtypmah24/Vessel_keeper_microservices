package com.marine.voyage_service.dto.voyage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a voyage response.
 */
@JsonSerialize
public record VoyageResponseDto(@JsonProperty Long id,
                                @JsonProperty String portOfLoading,
                                @JsonProperty String portOfDischarging,
                                @JsonProperty LocalDate startDate,
                                @JsonProperty LocalDate endDate) {
}