package com.example.crew_service.service.seaman;

import com.example.crew_service.dto.RecordOfServiceRequestDto;
import com.example.crew_service.entity.seaman.RecordOfService;
import com.example.crew_service.entity.seaman.Seaman;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.mapper.RecordOfServiceMapper;
import com.example.crew_service.repository.RecordOfServiceRepository;
import com.example.crew_service.repository.SeamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RecordOfServiceService {

    private final RecordOfServiceRepository repository;
    private final RecordOfServiceMapper mapper;
    private final SeamanRepository seamanRepository;


    @Autowired
    public RecordOfServiceService(RecordOfServiceRepository repository, RecordOfServiceMapper mapper, SeamanRepository seamanRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.seamanRepository = seamanRepository;
    }


    @Transactional
    public void addRecordOfService(Seaman seaman, Long imoNumber, String comment) {
        RecordOfServiceRequestDto candidate = createRequestDto(seaman, imoNumber, comment);
        RecordOfService record = mapper.recordOfServiceRequestDtoToRecordOfService(candidate);
        seaman.addServiceRecord(record);
        repository.save(record);
    }


    @Transactional
    public void addServiceRecordManually(long seamanId, long imoNumber, String comment) throws SeamanException {
        Seaman seaman = seamanRepository.findById(seamanId).orElseThrow(() -> new SeamanException("There is no seaman with id: " + seamanId));
        addRecordOfService(seaman, imoNumber, comment);
    }


    private RecordOfServiceRequestDto createRequestDto(Seaman seaman, Long imoNumber, String comment) {
        if (comment.isBlank()) comment = "Unknown";
        return new RecordOfServiceRequestDto(seaman, imoNumber, comment);
    }
}