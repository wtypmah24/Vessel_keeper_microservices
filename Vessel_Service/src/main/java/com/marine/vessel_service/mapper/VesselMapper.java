package com.marine.vessel_service.mapper;

import com.marine.vessel_service.dto.VesselRequestDto;
import com.marine.vessel_service.dto.VesselResponseDto;
import com.marine.vessel_service.entity.vessel.Vessel;
import com.marine.vessel_service.entity.vessel.VesselType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * The VesselMapper interface is responsible for mapping between Vessel objects and their corresponding DTOs.
 * It provides methods for converting VesselRequestDto and VesselResponseDto objects to Vessel entities, and vice versa.
 */
@Mapper(componentModel = "spring")
public interface VesselMapper {
    /**
     * Maps a VesselRequestDto object to a Vessel entity, ignoring crew and voyageId properties.
     *
     * @param vesselCandidate The VesselRequestDto object to be mapped.
     * @return The mapped Vessel entity.
     */
    @Mapping(target = "crew", ignore = true)
    @Mapping(target = "voyageId", ignore = true)
    Vessel vesselRequestDtoToVessel(VesselRequestDto vesselCandidate);

    /**
     * Maps a Vessel entity to a VesselResponseDto object.
     *
     * @param vessel The Vessel entity to be mapped.
     * @return The mapped VesselResponseDto object.
     */
    VesselResponseDto vesselToVesselResponseDto(Vessel vessel);

    /**
     * Maps a list of Vessel entities to a set of VesselResponseDto objects, ignoring crew property.
     *
     * @param vessels The list of Vessel entities to be mapped.
     * @return The set of mapped VesselResponseDto objects.
     */
    @Mapping(target = "crew", ignore = true)
    Set<VesselResponseDto> vesselsToVesselResponseDtos(List<Vessel> vessels);

    /**
     * Maps a vessel type string to a VesselType enum.
     *
     * @param type The vessel type string.
     * @return The corresponding VesselType enum.
     */
    default VesselType mapType(String type) {
        return VesselType.valueOf(type.toUpperCase());
    }
}
