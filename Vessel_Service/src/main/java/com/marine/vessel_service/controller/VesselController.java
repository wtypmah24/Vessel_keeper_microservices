package com.marine.vessel_service.controller;

import com.marine.vessel_service.dto.VesselRequestDto;
import com.marine.vessel_service.dto.VesselResponseDto;
import com.marine.vessel_service.exception.VesselException;
import com.marine.vessel_service.service.VesselService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/vessel")
@Tag(name = "Vessel", description = "Endpoints for vessel management")
public class VesselController {

    private final VesselService vesselService;

    @Autowired
    public VesselController(VesselService vesselService) {
        this.vesselService = vesselService;
    }

    @Operation(summary = "Add a new vessel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated")
    })
    @PostMapping("/add")
    public ResponseEntity<VesselResponseDto> addVessel(@RequestBody VesselRequestDto vesselRequestDto) throws VesselException {
        return ResponseEntity.status(HttpStatus.CREATED).body(vesselService.addVessel(vesselRequestDto));
    }

    @Operation(summary = "Get all vessels")
    @GetMapping("get_all")
    public ResponseEntity<Set<VesselResponseDto>> getAllVessels() {
        return ResponseEntity.status(HttpStatus.OK).body(vesselService.getAllVessels());
    }

    @Operation(summary = "Get vessel by IMO number")
    @GetMapping("/{imoNumber}")
    public VesselResponseDto getVesselByImoNumber(@PathVariable Long imoNumber) throws VesselException {
        return vesselService.getVesselByImo(imoNumber);
    }

    @Operation(summary = "Remove a vessel by IMO number")
    @DeleteMapping("/remove/{imoNumber}")
    public void removeVessel(@PathVariable long imoNumber) throws VesselException {
        vesselService.deleteVessel(imoNumber);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(VesselException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }
}