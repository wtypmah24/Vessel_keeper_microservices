package com.marine.voyage_service.service;

import com.marine.voyage_service.config.RabbitConfig;
import com.marine.voyage_service.dto.vessel.VesselResponseDto;
import com.marine.voyage_service.dto.voyage.VoyageAssignDto;
import com.marine.voyage_service.dto.voyage.VoyageRequestDto;
import com.marine.voyage_service.dto.voyage.VoyageResponseDto;
import com.marine.voyage_service.entity.voyage.Voyage;
import com.marine.voyage_service.exception.VoyageException;
import com.marine.voyage_service.mapper.VoyageMapper;
import com.marine.voyage_service.repository.VoyageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing voyages.
 */
@Service
public class VoyageService {
    private final VoyageRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final VoyageMapper mapper;

    /**
     * Constructor for VoyageService.
     *
     * @param repository     The repository for voyages
     * @param rabbitTemplate The RabbitTemplate for AMQP communication
     * @param mapper         The mapper for converting DTOs and entities
     */
    @Autowired
    public VoyageService(VoyageRepository repository, RabbitTemplate rabbitTemplate, VoyageMapper mapper) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    /**
     * Adds a new voyage.
     *
     * @param candidate The voyage request DTO
     * @return The added voyage response DTO
     * @throws VoyageException if the provided voyage request DTO is invalid
     */
    @Transactional
    public VoyageResponseDto addNewVoyage(VoyageRequestDto candidate) throws VoyageException {
        voyageCandidateCheck(candidate);
        return mapper.voyageToVoyageResponseDto(repository.save(mapper.voyageRequestDtoToVoyage(candidate)));
    }

    /**
     * Retrieves all voyages.
     *
     * @return List of all voyage response DTOs
     */
    @Transactional
    public List<VoyageResponseDto> getAllVoyages() {
        return mapper.voyagesToVoyageResponseDtos(repository.findAll());
    }

    /**
     * Retrieves voyages by discharging port and end date.
     *
     * @param dischargingPort The port of discharging
     * @param endDate         The end date
     * @return List of voyage response DTOs matching the criteria
     */
    @Transactional
    public List<VoyageResponseDto> getVoyagesByDischargingPortAndEndDate(String dischargingPort, LocalDate endDate) {
        return mapper.voyagesToVoyageResponseDtos(
                repository.findByPortOfDischargingAndEndDateAndIsVacantIsTrue(dischargingPort, endDate)
        );
    }

    /**
     * Retrieves voyages by loading port and start date.
     *
     * @param loadingPort The port of loading
     * @param startDate   The start date
     * @return List of voyage response DTOs matching the criteria
     */
    @Transactional
    public List<VoyageResponseDto> getVoyagesByLoadingPortAndStartDate(String loadingPort, LocalDate startDate) {
        return mapper.voyagesToVoyageResponseDtos(
                repository.findByPortOfLoadingAndStartDateAndIsVacantIsTrue(loadingPort, startDate)
        );
    }

    /**
     * Retrieves all available voyages.
     *
     * @return List of all available voyage response DTOs
     */
    @Transactional
    public List<VoyageResponseDto> getAllAvailableVoyages() {
        return mapper.voyagesToVoyageResponseDtos(repository.findByIsVacantIsTrue());
    }

    /**
     * Finds an applicable vessel for the given voyage ID.
     * It retrieves a vacant voyage based on the provided ID, then finds another voyage with a matching loading port
     * and start date where the vacancy is false. It then sends a message to the voyage queue to find the applicable vessel.
     *
     * @param voyageId The ID of the voyage
     * @return The applicable vessel response DTO
     */
    public VesselResponseDto findApplicableVessel(Long voyageId) {
        Voyage vacantVoyage = repository.findById(voyageId).orElseThrow();
        String loadingPort = vacantVoyage.getPortOfLoading();
        LocalDate startDate = vacantVoyage.getStartDate();

        Voyage targetVoyage = repository.findByPortOfDischargingAndEndDateAndIsVacantIsFalse(loadingPort, startDate).orElseThrow();
        Long id = targetVoyage.getId();

        return rabbitTemplate.convertSendAndReceiveAsType(RabbitConfig.VOYAGE_QUEUE, id, ParameterizedTypeReference.forType(VesselResponseDto.class));
    }

    /**
     * Assigns a voyage to a vessel based on the provided VoyageAssignDto.
     * It sets the vacancy status of the voyage to false and sends a message to the vessel queue to assign the voyage to a vessel.
     *
     * @param ids The VoyageAssignDto containing the voyage ID and vessel ID
     * @return The response DTO of the assigned vessel
     * @throws VoyageException if there is no voyage with the provided ID or if there is no vessel with the provided IMO number
     */
    @Transactional
    public VesselResponseDto assignVoyageToVessel(VoyageAssignDto ids) throws VoyageException {

        Voyage voyage = repository.findById(ids.voyageId())
                .orElseThrow(() -> new VoyageException("There is mo voyage with id: " + ids.voyageId()));
        voyage.setVacant(false);

        VesselResponseDto responseDto = rabbitTemplate
                .convertSendAndReceiveAsType
                        (RabbitConfig.VESSEL_QUEUE,
                                ids,
                                ParameterizedTypeReference.forType(VesselResponseDto.class));

        if (responseDto.imoNumber() == null) {
            voyage.setVacant(true);
            throw new VoyageException("There is no vessel with imo: " + ids.imoNumber());
        }
        return responseDto;
    }

    /**
     * Checks the validity of the provided voyage request DTO.
     *
     * @param candidate The voyage request DTO to be validated
     * @throws VoyageException if the provided voyage request DTO is invalid
     */
    private void voyageCandidateCheck(VoyageRequestDto candidate) throws VoyageException {
        if (candidate == null) throw new VoyageException("You didn't provide voyage");
        if (candidate.portOfDischarging().isBlank())
            throw new VoyageException("You didn't provide port of discharging");
        if (candidate.portOfLoading().isBlank()) throw new VoyageException("You didn't provide port of loading");
        if (candidate.startDate().isBefore(LocalDate.now()))
            throw new VoyageException("Provided voyage is expired. Start date is " + candidate.startDate());
        if (candidate.endDate().isBefore(candidate.startDate()))
            throw new VoyageException("End date must be after start date ");
    }
}