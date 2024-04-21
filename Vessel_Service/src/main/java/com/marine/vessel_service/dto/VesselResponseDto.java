package com.marine.vessel_service.dto;
/**
 * The VesselResponseDto class represents a data transfer object for retrieving vessel information.
 * It encapsulates information about a vessel's IMONumber, name, vessel type, and associated voyage ID.
 */
public record VesselResponseDto(Long imoNumber, String name, String vesselType, Long voyageId) {
}
