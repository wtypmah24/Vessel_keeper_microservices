package com.marine.vessel_service.dto;
/**
 * The VesselRequestDto class represents a data transfer object for creating or updating vessel information.
 * It encapsulates information about a vessel's IMONumber, name, and vessel type.
 */
public record VesselRequestDto(Long imoNumber, String name, String vesselType) {
}