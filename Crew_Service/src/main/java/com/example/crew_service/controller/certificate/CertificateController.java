package com.example.crew_service.controller.certificate;

import com.example.crew_service.dto.certificate.CertificateRequestDto;
import com.example.crew_service.dto.certificate.CertificateResponseDto;
import com.example.crew_service.exception.SeamanCertificateException;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.service.seaman.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/certificates")
@Tag(name = "Certificate", description = "Endpoints for crew certificates management")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Operation(
            summary = "Add certificate to a seaman",
            description = "Allows you add a certificate to a seaman. You can't add a certificate without connecting it to a seaman."
    )
    @PostMapping("/addCertificate/{seamanId}")
    public ResponseEntity<Set<CertificateResponseDto>> addCertificateToSeaman(@RequestBody CertificateRequestDto certificateCandidate,
                                                                              @PathVariable long seamanId) throws SeamanException, SeamanCertificateException {
        return ResponseEntity.status(HttpStatus.CREATED).body(certificateService.addCertificateToSeaman(certificateCandidate, seamanId));
    }

    @Operation(summary = "Remove seaman's certificate")
    @DeleteMapping("/removeCertificate/{certificateId}")
    public ResponseEntity<Set<CertificateResponseDto>> removeCertificate(@PathVariable long certificateId) throws SeamanCertificateException {
        return ResponseEntity.status(HttpStatus.OK).body(certificateService.deleteCertificate(certificateId));
    }
}
