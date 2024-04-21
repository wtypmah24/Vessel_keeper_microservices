package com.example.crew_service.mapper;

import com.example.crew_service.dto.RecordOfServiceRequestDto;
import com.example.crew_service.entity.seaman.RecordOfService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RecordOfServiceMapper {

    @Mapping(target = "id", ignore = true)
    RecordOfService recordOfServiceRequestDtoToRecordOfService(RecordOfServiceRequestDto candidate);
}
