package com.example.vesselfinder_service.service;

import com.example.vesselfinder_service.dto.VesselFinderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The PositionService class provides functionality to generate a static Google Maps URL
 * with markers for the positions of vessels.
 */

@Service
public class PositionService {
    private final VesselFinderService service;
    private final String googleApi;

    /**
     * Constructs a PositionService with the specified dependencies.
     *
     * @param service   The service for retrieving real-time vessel information.
     * @param googleApi The Google Maps API key.
     */
    @Autowired
    public PositionService(VesselFinderService service, @Value("${google_api}") String googleApi) {
        this.service = service;
        this.googleApi = googleApi;
    }

    /**
     * Generates a static Google Maps URL with markers for the positions of vessels.
     *
     * @param imoNumbers The list of IMO numbers of vessels.
     * @return The URL of the static Google Map.
     */
    public String generateStaticGoogleMapUrl(List<Long> imoNumbers) {
        List<String> positions = getVesselsPositions(imoNumbers);
        StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?");
        builder.append("center=").append("0,0"); // Set center to first location
        builder.append("&zoom="); // Adjust zoom level as needed
        builder.append("&size=1200x1200"); // Adjust map size as needed
        builder.append("&markers=color:red|"); // Marker color and separator
        for (String position : positions) {
            builder.append(position).append("|");
        }
        builder.append("&key=").append(googleApi);
        return builder.toString();
    }

    /**
     * Retrieves the positions of vessels using the VesselFinder service.
     *
     * @param imoNumbers The list of IMO numbers of vessels.
     * @return A list of strings representing vessel positions in latitude, longitude format.
     */
    private List<String> getVesselsPositions(List<Long> imoNumbers) {
        List<VesselFinderResponseDto> info = service.getRealTimeInfo(imoNumbers);

        List<String> positions = new ArrayList<>();

        info.forEach(i -> {
            double latitude = i.getAis().getLatitude();
            double longitude = i.getAis().getLongitude();
            String position = latitude + "," + longitude;
            positions.add(position);
        });

        return positions;
    }
}