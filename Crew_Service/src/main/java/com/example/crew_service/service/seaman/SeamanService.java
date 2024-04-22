package com.example.crew_service.service.seaman;

import com.example.crew_service.config.RabbitConfig;
import com.example.crew_service.dto.seaman.CrewDto;
import com.example.crew_service.dto.seaman.SeamanRequestDto;
import com.example.crew_service.dto.seaman.SeamanResponseDto;
import com.example.crew_service.entity.seaman.Seaman;
import com.example.crew_service.exception.SeamanCertificateException;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.exception.VesselException;
import com.example.crew_service.mapper.SeamanMapper;
import com.example.crew_service.repository.SeamanRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing seamen.
 */
@Service
public class SeamanService {
    private final SeamanMapper seamanMapper;
    private final SeamanRepository seamanRepository;
    private final RecordOfServiceService recordService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructs a SeamanService with the given dependencies.
     *
     * @param seamanMapper     The mapper for mapping between DTOs and entities related to seamen
     * @param seamanRepository The repository for managing seaman entities
     * @param recordService    The service for managing records of service for seamen
     * @param rabbitTemplate   The RabbitTemplate for sending messages to queues
     */
    @Autowired
    public SeamanService(SeamanMapper seamanMapper,
                         SeamanRepository seamanRepository,
                         RecordOfServiceService recordService, RabbitTemplate rabbitTemplate) {
        this.seamanMapper = seamanMapper;
        this.seamanRepository = seamanRepository;
        this.recordService = recordService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Retrieves all seamen.
     *
     * @return List of SeamanResponseDto representing all seamen
     */
    @Transactional
    public List<SeamanResponseDto> getAllSeamen() {
        return seamanRepository.findAll().stream().map(seamanMapper::seamanToSeamanResponseDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a seaman by ID.
     *
     * @param id The ID of the seaman to retrieve
     * @return The SeamanResponseDto representing the seaman with the specified ID
     * @throws SeamanException if the seaman with the provided ID is not found
     */
    @Transactional
    public SeamanResponseDto getSeamanById(long id) throws SeamanException {
        return seamanMapper.seamanToSeamanResponseDto(seamanRepository.findById(id).orElseThrow(() -> new SeamanException("There is no seaman  with id: " + id)));
    }

    /**
     * Adds a new seaman to the labor pool.
     *
     * @param candidate The SeamanRequestDto representing the seaman to be added
     * @return The SeamanResponseDto representing the added seaman
     * @throws SeamanException if the seaman candidate is null
     */
    @Transactional
    public SeamanResponseDto addSeamanToLaborPool(SeamanRequestDto candidate) throws SeamanException {
        if (candidate == null) throw new SeamanException("You didn't provide seaman candidate!");
        return seamanMapper.seamanToSeamanResponseDto(seamanRepository.save(seamanMapper.seamanRequestDtoToSeaman(candidate)));
    }

    /**
     * Removes a seaman from the labor pool.
     *
     * @param seamanId The ID of the seaman to be removed
     * @throws SeamanException if the seaman with the provided ID is not found
     */
    @Transactional
    public void removeSeamanFromLaborPool(long seamanId) throws SeamanException {
        seamanRepository.delete(seamanRepository.findById(seamanId).orElseThrow(() -> new SeamanException("There is no seaman with id: " + seamanId)));
    }

    /**
     * Hires a seaman onto a vessel.
     *
     * @param crewDto The CrewDto representing the crew information
     * @return The SeamanResponseDto representing the hired seaman
     * @throws SeamanException            if the seaman or its certificates are invalid
     * @throws SeamanCertificateException if the seaman does not have valid certificates
     * @throws VesselException            if there is no vessel with the provided IMO number
     */
    @Transactional
    public SeamanResponseDto hireSeaman(CrewDto crewDto) throws SeamanException, SeamanCertificateException, VesselException {
        Seaman seaman = seamanRepository.findById(crewDto.seamanId()).orElseThrow();
        certificateCheck(seaman);
        seaman.setHasJob(true);

        Boolean response = rabbitTemplate.convertSendAndReceiveAsType(RabbitConfig.HIRE_CREW_QUEUE, crewDto, ParameterizedTypeReference.forType(Boolean.class));
        if (!Boolean.TRUE.equals(response)) {
            seaman.setHasJob(false);
            throw new VesselException("There is no vessel with imo: " + crewDto.imoNumber());
        }
        return seamanMapper.seamanToSeamanResponseDto(seaman);
    }

    /**
     * Signs off a seaman from a vessel.
     *
     * @param crewDto The CrewDto representing the crew information
     * @param comment Any additional comment for signing off
     * @return The SeamanResponseDto representing the signed off seaman
     * @throws SeamanException if the seaman is not found or already signed off
     * @throws VesselException if there is no vessel with the provided IMO number
     */
    @Transactional
    public SeamanResponseDto signOffSeaman(CrewDto crewDto, String comment) throws SeamanException, VesselException {
        Seaman seaman = seamanRepository.findById(crewDto.seamanId()).orElseThrow(() -> new SeamanException("There is no seaman with id: " + crewDto.seamanId()));
        seaman.setHasJob(false);
        recordService.addRecordOfService(seaman, crewDto.imoNumber(), comment);

        Boolean response = rabbitTemplate.convertSendAndReceiveAsType(RabbitConfig.FIRE_CREW_QUEUE, crewDto, ParameterizedTypeReference.forType(Boolean.class));
        if (!Boolean.TRUE.equals(response)) {
            seaman.setHasJob(true);
            throw new VesselException("There is no vessel with imo: " + crewDto.imoNumber());
        }
        return seamanMapper.seamanToSeamanResponseDto(seaman);
    }

    /**
     * Checks if the seaman has valid certificates and is not already on a vessel.
     *
     * @param seaman The seaman to be checked
     * @throws SeamanException            if the seaman is already on a vessel
     * @throws SeamanCertificateException if the seaman does not have valid certificates
     */
    private void certificateCheck(Seaman seaman) throws SeamanException, SeamanCertificateException {
        if (!hasCertificate(seaman)) throw new SeamanCertificateException("Candidate has no certificates!");
        if (!isCertificateUpdate(seaman))
            throw new SeamanCertificateException("Candidate's certificates are not up to date!");
        if (seaman.isHasJob()) throw new SeamanException("Candidate is already on a vessel!");
    }

    /**
     * Checks if the seaman has certificates.
     *
     * @param seaman The seaman to be checked
     * @return true if the seaman has certificates, otherwise false
     */
    private boolean hasCertificate(Seaman seaman) {
        return !seaman.getCertificates().isEmpty();
    }

    /**
     * Checks if the seaman's certificates are up to date.
     *
     * @param seaman The seaman to be checked
     * @return true if all certificates are up to date, otherwise false
     */
    private boolean isCertificateUpdate(Seaman seaman) {
        return seaman.getCertificates().stream().allMatch(c -> c.getExpireDate().isAfter(LocalDate.now().plusMonths(4)));
    }
}