package com.marine.vessel_service.dto;
/**
 * The CrewDto class represents a data transfer object for crew members.
 * It encapsulates information about a crew member's IMONumber and SeamanId.
 */
public record CrewDto(Long imoNumber, Long seamanId) {
}
