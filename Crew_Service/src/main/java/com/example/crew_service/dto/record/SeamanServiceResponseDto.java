package com.example.crew_service.dto.record;

/**
 * Data transfer object (DTO) for the response from the seaman service.
 */
public record SeamanServiceResponseDto(long id, Long imoNumber, String comment) {
}
