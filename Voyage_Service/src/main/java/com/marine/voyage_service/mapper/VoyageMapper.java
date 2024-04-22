package com.marine.voyage_service.mapper;

import com.marine.voyage_service.dto.voyage.VoyageRequestDto;
import com.marine.voyage_service.dto.voyage.VoyageResponseDto;
import com.marine.voyage_service.entity.voyage.Voyage;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for mapping between Voyage-related DTOs and entities.
 */
@Mapper(componentModel = "spring")
public interface VoyageMapper {
    /**
     * Maps a VoyageRequestDto to a Voyage entity.
     *
     * @param candidate The VoyageRequestDto to map
     * @return The mapped Voyage entity
     */
    Voyage voyageRequestDtoToVoyage(VoyageRequestDto candidate);

    /**
     * Maps a Voyage entity to a VoyageResponseDto.
     *
     * @param voyage The Voyage entity to map
     * @return The mapped VoyageResponseDto
     */
    VoyageResponseDto voyageToVoyageResponseDto(Voyage voyage);

    /**
     * Maps a list of Voyage entities to a list of VoyageResponseDto objects.
     *
     * @param voyages The list of Voyage entities to map
     * @return The list of mapped VoyageResponseDto objects
     */
    List<VoyageResponseDto> voyagesToVoyageResponseDtos(List<Voyage> voyages);
}
