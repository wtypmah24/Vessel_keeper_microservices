package com.marine.vessel_service.service;

import com.marine.vessel_service.dto.VesselRequestDto;
import com.marine.vessel_service.dto.VesselResponseDto;
import com.marine.vessel_service.entity.vessel.Vessel;
import com.marine.vessel_service.exception.VesselException;
import com.marine.vessel_service.mapper.VesselMapper;
import com.marine.vessel_service.repository.VesselRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * The VesselService class handles operations related to vessel management.
 * It provides methods for adding, deleting, and retrieving vessels.
 */
@Service
public class VesselService {
    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper;

    /**
     * Constructs a new VesselService with the specified VesselRepository and VesselMapper.
     *
     * @param vesselRepository The repository for accessing vessel data.
     * @param vesselMapper     The mapper for converting between vessel DTOs and entities.
     */
    @Autowired
    public VesselService(VesselRepository vesselRepository,
                         VesselMapper vesselMapper) {
        this.vesselRepository = vesselRepository;
        this.vesselMapper = vesselMapper;
    }

    /**
     * Adds a new vessel using the information provided in the VesselRequestDto.
     *
     * @param candidate The DTO containing the details of the vessel to be added.
     * @return The DTO representing the newly added vessel.
     * @throws VesselException if the candidate vessel information is invalid or if there is an error saving the vessel.
     */
    @Transactional
    public VesselResponseDto addVessel(VesselRequestDto candidate) throws VesselException {
        checkVesselCandidate(candidate);
        return vesselMapper
                .vesselToVesselResponseDto(
                        vesselRepository.save(vesselMapper.vesselRequestDtoToVessel(candidate)));
    }

    /**
     * Deletes the vessel with the specified IMO number.
     *
     * @param imoNumber The IMO number of the vessel to be deleted.
     * @throws VesselException if the vessel with the specified IMO number does not exist.
     */
    @Transactional
    public void deleteVessel(long imoNumber) throws VesselException {
        Vessel vessel = vesselRepository.findByImoNumber(imoNumber)
                .orElseThrow(() -> new VesselException("There is mo vessel with IMO number: " + imoNumber));
        vesselRepository.delete(vessel);
    }

    /**
     * Retrieves all vessels and returns them as a set of VesselResponseDto objects.
     *
     * @return A set of DTOs representing all vessels.
     */
    public Set<VesselResponseDto> getAllVessels() {
        return vesselMapper.vesselsToVesselResponseDtos(vesselRepository.findAll());
    }

    /**
     * Retrieves the vessel with the specified IMO number and returns it as a VesselResponseDto.
     *
     * @param imoNumber The IMO number of the vessel to retrieve.
     * @return The DTO representing the vessel with the specified IMO number.
     * @throws VesselException if the vessel with the specified IMO number does not exist.
     */
    public VesselResponseDto getVesselByImo(Long imoNumber) throws VesselException {
        return vesselMapper.vesselToVesselResponseDto(vesselRepository.findByImoNumber(imoNumber)
                .orElseThrow(() -> new VesselException("There is mo vessel with IMO number: " + imoNumber)));
    }

    /**
     * Checks if the candidate vessel information is valid.
     *
     * @param candidate The DTO containing the vessel information to be validated.
     * @throws VesselException if the candidate vessel information is invalid.
     */
    private void checkVesselCandidate(VesselRequestDto candidate) throws VesselException {
        if (candidate == null) throw new VesselException("You didn't provide vessel.");
        if (candidate.imoNumber() == null) throw new VesselException("You didn't provide IMO number.");
        if (candidate.name().isBlank()) throw new VesselException("You didn't provide vessel name.");
    }
}
