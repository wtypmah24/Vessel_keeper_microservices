package com.marine.vessel_service.service;

import com.marine.vessel_service.config.RabbitConfig;
import com.marine.vessel_service.dto.CrewDto;
import com.marine.vessel_service.entity.vessel.Vessel;
import com.marine.vessel_service.repository.VesselRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The CrewService class handles operations related to crew management for vessels.
 * It listens for messages on RabbitMQ queues to hire or fire crew members for vessels.
 */
@Service
@EnableRabbit
public class CrewService {
    private final VesselRepository repository;

    /**
     * Constructs a new CrewService with the specified VesselRepository.
     *
     * @param repository The repository for accessing vessel data.
     */
    @Autowired
    public CrewService(VesselRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds a seaman to the crew of a vessel based on the received message payload.
     *
     * @param crewDto The DTO containing the IMO number of the vessel and the ID of the seaman to be added.
     * @return True if the seaman was successfully added to the crew, otherwise false.
     */
    @Transactional
    @RabbitListener(queues = RabbitConfig.HIRE_CREW_QUEUE)
    public Boolean addSeamanToCrew(@Payload CrewDto crewDto) {
        Vessel vessel = repository.findByImoNumber(crewDto.imoNumber()).orElse(null);
        if (vessel != null) {
            return vessel.addSeamanToCrew(crewDto.seamanId());
        } else {
            return false;
        }
    }

    /**
     * Removes a seaman from the crew of a vessel based on the received message payload.
     *
     * @param crewDto The DTO containing the IMO number of the vessel and the ID of the seaman to be removed.
     * @return True if the seaman was successfully removed from the crew, otherwise false.
     */
    @Transactional
    @RabbitListener(queues = RabbitConfig.FIRE_CREW_QUEUE)
    public Boolean removeSeamanFromCrew(@Payload CrewDto crewDto) {
        Vessel vessel = repository.findByImoNumber(crewDto.imoNumber()).orElse(null);
        if (vessel != null) {
            return vessel.removeSeamanFromCrew(crewDto.seamanId());
        } else {
            return false;
        }
    }
}