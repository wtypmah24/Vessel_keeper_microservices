package com.marine.voyage_service.dto.voyage;

/**
 * Data Transfer Object (DTO) representing the assignment of a voyage to a vessel.
 */
public record VoyageAssignDto(Long voyageId, Long imoNumber) {
}
