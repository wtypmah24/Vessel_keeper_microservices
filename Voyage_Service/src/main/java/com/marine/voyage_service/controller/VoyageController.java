package com.marine.voyage_service.controller;

import com.marine.voyage_service.dto.VesselResponseDto;
import com.marine.voyage_service.dto.VoyageAssignDto;
import com.marine.voyage_service.dto.VoyageRequestDto;
import com.marine.voyage_service.dto.VoyageResponseDto;
import com.marine.voyage_service.exception.VoyageException;
import com.marine.voyage_service.service.VoyageService;
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

    @PostMapping("/add")
    public ResponseEntity<VoyageResponseDto> addVoyage(@RequestBody VoyageRequestDto voyageRequestDto) throws VoyageException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addNewVoyage(voyageRequestDto));
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<VoyageResponseDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllVoyages());
    }

    @GetMapping("/get_available")
    public ResponseEntity<List<VoyageResponseDto>> getAllAvailable() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllAvailableVoyages());
    }

    @GetMapping("/get_end")
    public ResponseEntity<List<VoyageResponseDto>> getVoyagesByDischargingPortAndEndDate(@RequestParam("dischargingPort") String dischargingPort,
                                                                                         @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getVoyagesByDischargingPortAndEndDate(dischargingPort, endDate));
    }

    @GetMapping("/get_start")
    public ResponseEntity<List<VoyageResponseDto>> getVoyagesByLoadingPortAndStartDate(@RequestParam("loadingPort") String loadingPort,
                                                                                       @RequestParam("startDate") LocalDate startDate) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getVoyagesByLoadingPortAndStartDate(loadingPort, startDate));
    }

    @GetMapping("/findApplicable/{id}")
    public VesselResponseDto findApplicableVessel(@PathVariable Long id) {
        return service.findApplicableVessel(id);
    }

    @PostMapping("/assign")
    public ResponseEntity<VesselResponseDto>  assignVoyageToVessel(@RequestBody VoyageAssignDto ids) throws VoyageException {
        return ResponseEntity.status(HttpStatus.OK).body(service.assignVoyageToVessel(ids));
    }

   @ExceptionHandler
    public ResponseEntity<ErrorMessage> voyageHandleException(VoyageException exception) {
       return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new ErrorMessage(exception.getMessage()));
   }
}
