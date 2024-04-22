package com.marine.voyage_service.controller;

import com.marine.voyage_service.dto.vessel.VesselResponseDto;
import com.marine.voyage_service.dto.voyage.VoyageAssignDto;
import com.marine.voyage_service.dto.voyage.VoyageRequestDto;
import com.marine.voyage_service.dto.voyage.VoyageResponseDto;
import com.marine.voyage_service.exception.VoyageException;
import com.marine.voyage_service.service.VoyageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voyage")
public class VoyageController {
    private final VoyageService service;

    @Autowired
    public VoyageController(VoyageService service) {
        this.service = service;
    }

    @Operation(summary = "Add a new voyage")
    @PostMapping("/add")
    public ResponseEntity<VoyageResponseDto> addVoyage(@RequestBody VoyageRequestDto voyageRequestDto) throws VoyageException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addNewVoyage(voyageRequestDto));
    }

    @Operation(summary = "Get all voyages")
    @GetMapping("/get_all")
    public ResponseEntity<List<VoyageResponseDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllVoyages());
    }

    @Operation(summary = "Get all available voyages")
    @GetMapping("/get_available")
    public ResponseEntity<List<VoyageResponseDto>> getAllAvailable() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllAvailableVoyages());
    }

    @Operation(summary = "Get voyages by discharging port and end date")
    @GetMapping("/get_end")
    public ResponseEntity<List<VoyageResponseDto>> getVoyagesByDischargingPortAndEndDate(@RequestParam("dischargingPort") String dischargingPort,
                                                                                         @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getVoyagesByDischargingPortAndEndDate(dischargingPort, endDate));
    }

    @Operation(summary = "Get voyages by loading port and start date")
    @GetMapping("/get_start")
    public ResponseEntity<List<VoyageResponseDto>> getVoyagesByLoadingPortAndStartDate(@RequestParam("loadingPort") String loadingPort,
                                                                                       @RequestParam("startDate") LocalDate startDate) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getVoyagesByLoadingPortAndStartDate(loadingPort, startDate));
    }

    @Operation(summary = "Find applicable vessel for a voyage. Based on vessel's discharging port and date.")
    @GetMapping("/findApplicable/{id}")
    public VesselResponseDto findApplicableVessel(@PathVariable Long id) {
        return service.findApplicableVessel(id);
    }

    @Operation(summary = "Assign a voyage to a vessel")
    @PostMapping("/assign")
    public ResponseEntity<VesselResponseDto> assignVoyageToVessel(@RequestBody VoyageAssignDto ids) throws VoyageException {
        return ResponseEntity.status(HttpStatus.OK).body(service.assignVoyageToVessel(ids));
    }

    @ExceptionHandler
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    public ResponseEntity<ErrorMessage> voyageHandleException(VoyageException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
