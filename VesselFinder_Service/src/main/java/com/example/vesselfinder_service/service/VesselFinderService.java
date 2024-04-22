package com.example.vesselfinder_service.service;

import com.example.vesselfinder_service.dto.VesselFinderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The VesselFinderService class provides functionality to retrieve real-time vessel information from a remote service.
 */
@Service
public class VesselFinderService {
    private final RestTemplate restTemplate;
    private final String apiBaseUrl;
    private final String finderApiKey = System.getenv("VESSEL_FINDER_API_KEY");

    /**
     * Constructs a VesselFinderService with the specified dependencies.
     *
     * @param restTemplate The RestTemplate for making HTTP requests.
     * @param apiBaseUrl   The base URL of the vessel finder API.
     */
    @Autowired
    public VesselFinderService(RestTemplate restTemplate,
                               @Value("${vessel_finder_base_url}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Retrieves real-time information for vessels with the specified IMO numbers.
     *
     * @param imoNumbers The list of IMO numbers of vessels.
     * @return A list of VesselFinderResponseDto representing real-time information for the vessels.
     */
    public List<VesselFinderResponseDto> getRealTimeInfo(List<Long> imoNumbers) {
        String imo = imoNumbers.stream().map(String::valueOf).collect(Collectors.joining(","));
        String url = apiBaseUrl + imo;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("userkey", finderApiKey)
                .queryParam("imo", imo);
        VesselFinderResponseDto[] response = restTemplate.getForObject(builder.toUriString(), VesselFinderResponseDto[].class);
        assert response != null;
        return Arrays.asList(response);
    }
}
