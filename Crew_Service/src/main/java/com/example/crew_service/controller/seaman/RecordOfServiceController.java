package com.example.crew_service.controller.seaman;

import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.exception.VesselException;
import com.example.crew_service.service.seaman.RecordOfServiceService;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service_record")
public class RecordOfServiceController {
    private final RecordOfServiceService recordService;

    @Autowired
    public RecordOfServiceController(RecordOfServiceService recordService) {
        this.recordService = recordService;
    }

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