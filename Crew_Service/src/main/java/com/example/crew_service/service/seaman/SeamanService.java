package com.example.crew_service.service.seaman;

import com.example.crew_service.config.RabbitConfig;
import com.example.crew_service.dto.CrewDto;
import com.example.crew_service.dto.SeamanRequestDto;
import com.example.crew_service.dto.SeamanResponseDto;
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


@Service
public class SeamanService {
    private final SeamanMapper seamanMapper;
    private final SeamanRepository seamanRepository;
    private final RecordOfServiceService recordService;
    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public SeamanService(SeamanMapper seamanMapper,
                         SeamanRepository seamanRepository,
                         RecordOfServiceService recordService, RabbitTemplate rabbitTemplate) {
        this.seamanMapper = seamanMapper;
        this.seamanRepository = seamanRepository;
        this.recordService = recordService;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Transactional
    public List<SeamanResponseDto> getAllSeamen() {
        return seamanRepository.findAll().stream().map(seamanMapper::seamanToSeamanResponseDto).collect(Collectors.toList());
    }


    @Transactional
    public SeamanResponseDto getSeamanById(long id) throws SeamanException {
        return seamanMapper.seamanToSeamanResponseDto(seamanRepository.findById(id).orElseThrow(() -> new SeamanException("There is no seaman  with id: " + id)));
    }


    @Transactional
    public SeamanResponseDto addSeamanToLaborPool(SeamanRequestDto candidate) throws SeamanException {
        if (candidate == null) throw new SeamanException("You didn't provide seaman candidate!");
        return seamanMapper.seamanToSeamanResponseDto(seamanRepository.save(seamanMapper.seamanRequestDtoToSeaman(candidate)));
    }


    @Transactional
    public void removeSeamanFromLaborPool(long seamanId) throws SeamanException {
        seamanRepository.delete(seamanRepository.findById(seamanId).orElseThrow(() -> new SeamanException("There is no seaman with id: " + seamanId)));
    }


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

    private void certificateCheck(Seaman seaman) throws SeamanException, SeamanCertificateException {
        if (!hasCertificate(seaman)) throw new SeamanCertificateException("Candidate has no certificates!");
        if (!isCertificateUpdate(seaman))
            throw new SeamanCertificateException("Candidate's certificates are not up to date!");
        if (seaman.isHasJob()) throw new SeamanException("Candidate is already on a vessel!");
    }


    private boolean hasCertificate(Seaman seaman) {
        return !seaman.getCertificates().isEmpty();
    }

    private boolean isCertificateUpdate(Seaman seaman) {
        return seaman.getCertificates().stream().allMatch(c -> c.getExpireDate().isAfter(LocalDate.now().plusMonths(4)));
    }
}