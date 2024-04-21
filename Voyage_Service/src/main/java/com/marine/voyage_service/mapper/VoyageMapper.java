package com.marine.voyage_service.mapper;

import com.marine.voyage_service.dto.VoyageRequestDto;
import com.marine.voyage_service.dto.VoyageResponseDto;
import com.marine.voyage_service.entity.voyage.Voyage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoyageMapper {

    Voyage voyageRequestDtoToVoyage(VoyageRequestDto candidate);

    VoyageResponseDto voyageToVoyageResponseDto(Voyage voyage);

    List<VoyageResponseDto> voyagesToVoyageResponseDtos(List<Voyage> voyages);
}
