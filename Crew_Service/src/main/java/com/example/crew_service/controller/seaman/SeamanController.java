package com.example.crew_service.controller.seaman;

import com.example.crew_service.dto.seaman.CrewDto;
import com.example.crew_service.dto.seaman.SeamanRequestDto;
import com.example.crew_service.dto.seaman.SeamanResponseDto;
import com.example.crew_service.exception.SeamanCertificateException;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.exception.VesselException;
import com.example.crew_service.service.seaman.SeamanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Seaman Controller", description = "Here you can manage your labor pool.")
@RestController
@RequestMapping("/crew")
public class SeamanController {
    private final SeamanService seamanService;

    @Autowired
    public SeamanController(SeamanService seamanService) {
        this.seamanService = seamanService;
    }

    @Operation(
            summary = "Get all seamen"
    )
    @GetMapping
    public List<SeamanResponseDto> getAllSeamen() {
        return seamanService.getAllSeamen();
    }

    @Operation(
            summary = "Get seaman by id."
    )
    @GetMapping("/{id}")
    public SeamanResponseDto getSeamanById(@PathVariable long id) throws SeamanException {
        return seamanService.getSeamanById(id);
    }

    @Operation(
            summary = "Add seaman to labor pool",
            description = "Allows you add a seaman to labor pool without checking his certificates. Certificates check will be performed on signing on a seaman on a vessel."
    )
    @PostMapping("/create")
    public ResponseEntity<SeamanResponseDto> createSeaman(@RequestBody SeamanRequestDto seamanCandidate) throws SeamanException {
        return ResponseEntity.status(HttpStatus.CREATED).body(seamanService.addSeamanToLaborPool(seamanCandidate));
    }

    @Operation(
            summary = "Remove seaman from labor pool"
    )
    @DeleteMapping("/remove/{seamanId}")
    public void removeSeaman(@PathVariable long seamanId) throws SeamanException {
        seamanService.removeSeamanFromLaborPool(seamanId);
    }

    @Operation(
            summary = "Sign on a seaman on a vessel.",
            description = "Allows you sign on the seaman you choose on the vessel you choose."
    )
    @PostMapping("/hire/seaman")
    public SeamanResponseDto hireSeaman(@RequestBody CrewDto crewDto) throws SeamanException, SeamanCertificateException, VesselException {
        return seamanService.hireSeaman(crewDto);
    }

    @Operation(
            summary = "Sign off a seaman from a vessel.",
            description = "Allows you sign off the seaman you choose from the vessel. Seaman evaluation report is compulsory!"
    )
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
