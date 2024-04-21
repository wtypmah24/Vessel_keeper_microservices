package com.example.crew_service.mapper;

import com.example.crew_service.dto.SeamanRequestDto;
import com.example.crew_service.dto.SeamanResponseDto;
import com.example.crew_service.entity.seaman.Rank;
import com.example.crew_service.entity.seaman.Seaman;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SeamanMapper {

    @Mapping(target = "hasJob", ignore = true)
    @Mapping(target = "certificates", ignore = true)
    @Mapping(target = "recordOfServices", ignore = true)
    Seaman seamanRequestDtoToSeaman(SeamanRequestDto seamanCandidate);


    SeamanResponseDto seamanToSeamanResponseDto(Seaman seaman);


    Set<SeamanResponseDto> seamenToSeamenResponseDtos(Set<Seaman> seamanSet);


    default Rank mapRole(String rank) {
        return Rank.valueOf(rank.toUpperCase());
    }
}
