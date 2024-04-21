package com.example.crew_service.controller.seaman;

import com.example.crew_service.dto.CrewDto;
import com.example.crew_service.dto.SeamanRequestDto;
import com.example.crew_service.dto.SeamanResponseDto;
import com.example.crew_service.exception.SeamanCertificateException;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.exception.VesselException;
import com.example.crew_service.service.seaman.SeamanService;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crew")
public class SeamanController {
    private final SeamanService seamanService;

    @Autowired
    public SeamanController(SeamanService seamanService) {
        this.seamanService = seamanService;
    }


    @GetMapping
    public List<SeamanResponseDto> getAllSeamen() {
        return seamanService.getAllSeamen();
    }

    @GetMapping("/{id}")
    public SeamanResponseDto getSeamanById(@PathVariable long id) throws SeamanException {
        return seamanService.getSeamanById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<SeamanResponseDto> createSeaman(@RequestBody SeamanRequestDto seamanCandidate) throws SeamanException {
        return ResponseEntity.status(HttpStatus.CREATED).body(seamanService.addSeamanToLaborPool(seamanCandidate));
    }


    @DeleteMapping("/remove/{seamanId}")
    public void removeSeaman(@PathVariable long seamanId) throws SeamanException {
        seamanService.removeSeamanFromLaborPool(seamanId);
    }


    @PostMapping("/hire/seaman")
    public SeamanResponseDto hireSeaman(@RequestBody CrewDto crewDto) throws SeamanException, SeamanCertificateException, VesselException {
        return seamanService.hireSeaman(crewDto);
    }


    @PostMapping("/signOff/")
    public SeamanResponseDto signOffSeaman(@RequestBody CrewDto crewDto,
                                           @RequestParam("comment") String comment) throws SeamanException, VesselException {
        return seamanService.signOffSeaman(crewDto, comment);
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

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> certificateHandleException(SeamanCertificateException certificateException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(certificateException.getMessage()));
    }
}
