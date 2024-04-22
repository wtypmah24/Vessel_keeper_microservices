package com.example.crew_service.service.seaman;

import com.example.crew_service.dto.record.RecordOfServiceRequestDto;
import com.example.crew_service.entity.seaman.RecordOfService;
import com.example.crew_service.entity.seaman.Seaman;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.mapper.RecordOfServiceMapper;
import com.example.crew_service.repository.RecordOfServiceRepository;
import com.example.crew_service.repository.SeamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing records of service for seamen.
 */
@Service
public class RecordOfServiceService {

    private final RecordOfServiceRepository repository;
    private final RecordOfServiceMapper mapper;
    private final SeamanRepository seamanRepository;

    /**
     * Constructs a RecordOfServiceService with the given dependencies.
     *
     * @param repository       The repository for managing record of service entities
     * @param mapper           The mapper for mapping between DTOs and entities related to records of service
     * @param seamanRepository The repository for managing seaman entities
     */
    @Autowired
    public RecordOfServiceService(RecordOfServiceRepository repository, RecordOfServiceMapper mapper, SeamanRepository seamanRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.seamanRepository = seamanRepository;
    }

    /**
     * Adds a new record of service for the given seaman.
     *
     * @param seaman    The seaman for whom the record of service is added
     * @param imoNumber The IMO number of the vessel
     * @param comment   Any additional comment for the record
     */
    @Transactional
    public void addRecordOfService(Seaman seaman, Long imoNumber, String comment) {
        RecordOfServiceRequestDto candidate = createRequestDto(seaman, imoNumber, comment);
        RecordOfService record = mapper.recordOfServiceRequestDtoToRecordOfService(candidate);
        seaman.addServiceRecord(record);
        repository.save(record);
    }

    /**
     * Adds a new service record manually for the seaman with the provided ID.
     *
     * @param seamanId  The ID of the seaman for whom the record of service is added
     * @param imoNumber The IMO number of the vessel
     * @param comment   Any additional comment for the record
     * @throws SeamanException if the seaman with the provided ID is not found
     */
    @Transactional
    public void addServiceRecordManually(long seamanId, long imoNumber, String comment) throws SeamanException {
        Seaman seaman = seamanRepository.findById(seamanId).orElseThrow(() -> new SeamanException("There is no seaman with id: " + seamanId));
        addRecordOfService(seaman, imoNumber, comment);
    }

    /**
     * Creates a RecordOfServiceRequestDto from the given parameters.
     * If the comment is blank, it sets it to "Unknown".
     *
     * @param seaman    The seaman for the record of service
     * @param imoNumber The IMO number of the vessel
     * @param comment   The additional comment for the record
     * @return The created RecordOfServiceRequestDto
     */
    private RecordOfServiceRequestDto createRequestDto(Seaman seaman, Long imoNumber, String comment) {
        if (comment.isBlank()) comment = "Unknown";
        return new RecordOfServiceRequestDto(seaman, imoNumber, comment);
    }
}