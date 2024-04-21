package com.example.crew_service.controller.seaman;

import com.example.crew_service.dto.CertificateRequestDto;
import com.example.crew_service.dto.CertificateResponseDto;
import com.example.crew_service.exception.SeamanCertificateException;
import com.example.crew_service.exception.SeamanException;
import com.example.crew_service.service.seaman.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/addCertificate/{seamanId}")

    public ResponseEntity<Set<CertificateResponseDto>> addCertificateToSeaman(@RequestBody CertificateRequestDto certificateCandidate,
                                                                              @PathVariable long seamanId) throws SeamanException, SeamanCertificateException {
        return ResponseEntity.status(HttpStatus.CREATED).body(certificateService.addCertificateToSeaman(certificateCandidate, seamanId));
    }

    @DeleteMapping("/removeCertificate/{certificateId}")
    public ResponseEntity<Set<CertificateResponseDto>> removeCertificate(@PathVariable long certificateId) throws SeamanCertificateException {
        return ResponseEntity.status(HttpStatus.OK).body(certificateService.deleteCertificate(certificateId));
    }
}
