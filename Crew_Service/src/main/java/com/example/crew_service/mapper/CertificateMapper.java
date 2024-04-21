package com.example.crew_service.mapper;

import com.example.crew_service.dto.CertificateRequestDto;
import com.example.crew_service.dto.CertificateResponseDto;
import com.example.crew_service.entity.seaman.SeamanCertificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "seaman", ignore = true)
    SeamanCertificate certificateRequestDtoToCertificate(CertificateRequestDto certificateCandidate);


    CertificateResponseDto certificateToCertificateResponseDto(SeamanCertificate certificate);
}