package com.example.crew_service.mapper;

import com.example.crew_service.dto.seaman.SeamanRequestDto;
import com.example.crew_service.dto.seaman.SeamanResponseDto;
import com.example.crew_service.entity.seaman.Rank;
import com.example.crew_service.entity.seaman.Seaman;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

/**
 * Mapper interface for mapping between seaman DTOs and entities.
 */
@Mapper(componentModel = "spring")
public interface SeamanMapper {
    /**
     * Maps a seaman request DTO to a seaman entity.
     *
     * @param seamanCandidate The seaman request DTO
     * @return The mapped seaman entity
     */
    @Mapping(target = "hasJob", ignore = true)
    @Mapping(target = "certificates", ignore = true)
    @Mapping(target = "recordOfServices", ignore = true)
    Seaman seamanRequestDtoToSeaman(SeamanRequestDto seamanCandidate);

    /**
     * Maps a seaman entity to a seaman response DTO.
     *
     * @param seaman The seaman entity
     * @return The mapped seaman response DTO
     */
    SeamanResponseDto seamanToSeamanResponseDto(Seaman seaman);

    /**
     * Maps a string representation of rank to the Rank enum.
     *
     * @param rank The string representation of rank
     * @return The mapped Rank enum
     */
    default Rank mapRole(String rank) {
        return Rank.valueOf(rank.toUpperCase());
    }
}
