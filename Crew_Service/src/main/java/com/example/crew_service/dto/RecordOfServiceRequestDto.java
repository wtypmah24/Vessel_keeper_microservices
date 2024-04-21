package com.example.crew_service.dto;

import com.example.crew_service.entity.seaman.Seaman;

public record RecordOfServiceRequestDto(Seaman seaman, Long imoNumber, String comment) {
}
