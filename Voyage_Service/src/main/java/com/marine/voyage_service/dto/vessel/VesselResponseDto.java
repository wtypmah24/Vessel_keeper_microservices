package com.marine.voyage_service.dto.vessel;

/**
 * Data Transfer Object (DTO) representing a response for vessel information.
 */
public record VesselResponseDto(Long imoNumber, String name, String vesselType, Long voyageId) {
}
