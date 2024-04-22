package com.example.crew_service.mapper;

import com.example.crew_service.dto.record.RecordOfServiceRequestDto;
import com.example.crew_service.entity.seaman.RecordOfService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for mapping between record of service DTOs and entities.
 */
@Mapper(componentModel = "spring")
public interface RecordOfServiceMapper {
    /**
     * Maps a record of service request DTO to a record of service entity.
     *
     * @param candidate The record of service request DTO
     * @return The mapped record of service entity
     */
    @Mapping(target = "id", ignore = true)
    RecordOfService recordOfServiceRequestDtoToRecordOfService(RecordOfServiceRequestDto candidate);
}
