package com.example.vesselfinder_service.controller.finder;

import com.example.vesselfinder_service.dto.VesselFinderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/finder")
@Tag(name = "Vessel Finder controller.",
        description = "Here you can get real time info about your vessels from 3rd party API.")
public class VesselFinderController {
    private final VesselFinderClient vesselFinder;
    private final String finderApiKey = System.getenv("VESSEL_FINDER_API_KEY");

    @Autowired
    public VesselFinderController(VesselFinderClient vesselFinder) {
        this.vesselFinder = vesselFinder;
    }

    @GetMapping("/get_info")
    @Operation(summary = "Get info about one or more your vessels.",
            description = "Provide IMO numbers separated by coma.")
    public List<VesselFinderResponseDto> getRealTimeInfo(@RequestParam("imo") List<Long> imo) {
        String imoN = imo.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return vesselFinder.getInfo(finderApiKey, imoN);
    }

    @GetMapping("/check_status")
    @Operation(summary = "Check status of the prepaid VesselFinder API")
    public String status() {
        return vesselFinder.getStatus(finderApiKey);
    }
}