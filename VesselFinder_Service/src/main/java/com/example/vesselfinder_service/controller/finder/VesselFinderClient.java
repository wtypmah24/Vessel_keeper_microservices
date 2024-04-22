package com.example.vesselfinder_service.controller.finder;

import com.example.vesselfinder_service.dto.VesselFinderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "vesselFinderClient", url = "https://api.vesselfinder.com")
public interface VesselFinderClient {
    @GetMapping(path = "/vessels")
    List<VesselFinderResponseDto> getInfo(
            @RequestParam("userkey") String userKey,
            @RequestParam("imo") String imo
    );
}