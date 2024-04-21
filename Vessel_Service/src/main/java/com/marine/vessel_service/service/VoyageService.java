package com.marine.vessel_service.service;

import com.marine.vessel_service.config.RabbitConfig;
import com.marine.vessel_service.dto.VesselResponseDto;
import com.marine.vessel_service.dto.VoyageAssignDto;
import com.marine.vessel_service.entity.vessel.Vessel;
import com.marine.vessel_service.exception.VesselException;
import com.marine.vessel_service.mapper.VesselMapper;
import com.marine.vessel_service.repository.VesselRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The VoyageService class handles operations related to voyages and vessels.
 * It listens for messages from RabbitMQ to find vessels by voyage ID and assign voyages to vessels.
 */
@Service
@EnableRabbit
public class VoyageService {
    private final VesselRepository repository;
    private final VesselMapper mapper;

    /**
     * Constructs a new VoyageService with the specified VesselRepository and VesselMapper.
     *
     * @param repository The repository for accessing vessel data.
     * @param mapper     The mapper for converting between vessel entities and DTOs.
     */
    @Autowired
    public VoyageService(VesselRepository repository, VesselMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Listens for messages from RabbitMQ to find a vessel by voyage ID.
     * Retrieves the vessel associated with the specified voyage ID and returns its DTO representation.
     *
     * @param voyageId The ID of the voyage for which to find the associated vessel.
     * @return The DTO representing the vessel associated with the voyage ID.
     * @throws VesselException if there is no vessel associated with the specified voyage ID.
     */
    @RabbitListener(queues = RabbitConfig.VOYAGE_QUEUE)
    public VesselResponseDto findVesselByVoyageId(@Payload Long voyageId) throws VesselException {
        Vessel vessel = repository.findVesselsByVoyageId(voyageId).orElseThrow(() -> new VesselException("There is mo vessel with voyage number: " + voyageId));
        return mapper.vesselToVesselResponseDto(vessel);
    }

    /**
     * Listens for messages from RabbitMQ to assign a voyage to a vessel.
     * Assigns the specified voyage to the vessel with the given IMO number.
     *
     * @param ids The DTO containing the voyage ID and IMO number for the assignment.
     * @return The DTO representing the vessel with the assigned voyage.
     */

    @Transactional
    @RabbitListener(queues = RabbitConfig.VESSEL_QUEUE)
    public VesselResponseDto assignVoyageToVessel(@Payload VoyageAssignDto ids) {
        Vessel vessel = repository.findByImoNumberAndVoyageIdNull(ids.imoNumber()).orElse(null);
        if (vessel != null) {
            vessel.setVoyageId(ids.voyageId());
            return mapper.vesselToVesselResponseDto(vessel);
        } else {
            return new VesselResponseDto(null, "WRONG", "WRONG", null);
        }
    }
}