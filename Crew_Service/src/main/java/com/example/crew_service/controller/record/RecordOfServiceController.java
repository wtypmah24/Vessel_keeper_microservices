package com.example.crew_service.controller.record;

import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.exception.VesselException;
import com.example.crew_service.service.seaman.RecordOfServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service_record")
@Tag(name = "Seaman's record of services controller.",
        description = "Here you can add manually working experience to a seaman.")
public class RecordOfServiceController {
    private final RecordOfServiceService recordService;

    @Autowired
    public RecordOfServiceController(RecordOfServiceService recordService) {
        this.recordService = recordService;
    }

    @Operation(
            summary = "Add record of service to a seaman",
            description = "Allows you add a working experience to a seaman."
    )
    @PostMapping("/add")
    public void addServiceRecordManually(@RequestParam("seamanId") long seamanId,
                                         @RequestParam("imoNumber") long imoNumber,
                                         @RequestParam("comment") String comment) throws SeamanException {
        recordService.addServiceRecordManually(seamanId, imoNumber, comment);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> seamanHandleException(SeamanException seamanException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(seamanException.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> vesselHandleException(VesselException vesselException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(vesselException.getMessage()));
    }
}