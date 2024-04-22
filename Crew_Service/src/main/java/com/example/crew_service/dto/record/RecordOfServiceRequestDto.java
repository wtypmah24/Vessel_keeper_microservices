package com.example.crew_service.dto.record;

import com.example.crew_service.entity.seaman.Seaman;

/**
 * Data transfer object (DTO) for requesting a record of service.
 */
public record RecordOfServiceRequestDto(Seaman seaman, Long imoNumber, String comment) {
}
