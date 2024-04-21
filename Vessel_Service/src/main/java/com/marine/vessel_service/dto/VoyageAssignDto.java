package com.marine.vessel_service.dto;

/**
 * The VoyageAssignDto class represents a data transfer object for assigning a vessel to a voyage.
 * It encapsulates information about the voyage ID and the IMONumber of the vessel to be assigned.
 */
public record VoyageAssignDto(Long voyageId, Long imoNumber) {
}
