package com.marine.voyage_service.service;

import com.marine.voyage_service.config.RabbitConfig;
import com.marine.voyage_service.dto.VesselResponseDto;
import com.marine.voyage_service.dto.VoyageAssignDto;
import com.marine.voyage_service.dto.VoyageRequestDto;
import com.marine.voyage_service.dto.VoyageResponseDto;
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


@Service
public class VoyageService {
    private final VoyageRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final VoyageMapper mapper;

    @Autowired
    public VoyageService(VoyageRepository repository, RabbitTemplate rabbitTemplate, VoyageMapper mapper) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    @Transactional
    public VoyageResponseDto addNewVoyage(VoyageRequestDto candidate) throws VoyageException {
        voyageCandidateCheck(candidate);
        return mapper.voyageToVoyageResponseDto(repository.save(mapper.voyageRequestDtoToVoyage(candidate)));
    }

    @Transactional
    public List<VoyageResponseDto> getAllVoyages() {
        return mapper.voyagesToVoyageResponseDtos(repository.findAll());
    }

    @Transactional
    public List<VoyageResponseDto> getVoyagesByDischargingPortAndEndDate(String dischargingPort, LocalDate endDate) {
        return mapper.voyagesToVoyageResponseDtos(
                repository.findByPortOfDischargingAndEndDateAndIsVacantIsTrue(dischargingPort, endDate)
        );
    }

    @Transactional
    public List<VoyageResponseDto> getVoyagesByLoadingPortAndStartDate(String loadingPort, LocalDate startDate) {
        return mapper.voyagesToVoyageResponseDtos(
                repository.findByPortOfLoadingAndStartDateAndIsVacantIsTrue(loadingPort, startDate)
        );
    }

    @Transactional
    public List<VoyageResponseDto> getAllAvailableVoyages() {
        return mapper.voyagesToVoyageResponseDtos(repository.findByIsVacantIsTrue());
    }

    public VesselResponseDto findApplicableVessel(Long voyageId) {
        Voyage vacantVoyage = repository.findById(voyageId).orElseThrow();
        String loadingPort = vacantVoyage.getPortOfLoading();
        LocalDate startDate = vacantVoyage.getStartDate();

        Voyage targetVoyage = repository.findByPortOfDischargingAndEndDateAndIsVacantIsFalse(loadingPort, startDate).orElseThrow();
        Long id = targetVoyage.getId();

        return rabbitTemplate.convertSendAndReceiveAsType(RabbitConfig.VOYAGE_QUEUE, id, ParameterizedTypeReference.forType(VesselResponseDto.class));
    }

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