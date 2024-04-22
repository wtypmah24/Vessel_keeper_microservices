package com.example.vesselfinder_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the response from the Vessel Finder service.
 */
@Data
@NoArgsConstructor
public class VesselFinderResponseDto {
    @JsonProperty("AIS")
    private AISInfo ais;

    public void setAis(AISInfo ais) {
        this.ais = ais;
    }
    @Data
    public static class AISInfo {

        @JsonProperty("MMSI")
        private long mmsi;

        @JsonProperty("TIMESTAMP")
        private String timestamp;

        @JsonProperty("LATITUDE")
        private double latitude;

        @JsonProperty("LONGITUDE")
        private double longitude;

        @JsonProperty("COURSE")
        private double course;

        @JsonProperty("SPEED")
        private double speed;

        @JsonProperty("HEADING")
        private int heading;

        @JsonProperty("NAVSTAT")
        private int navstat;

        @JsonProperty("IMO")
        private long imo;

        @JsonProperty("NAME")
        private String name;

        @JsonProperty("CALLSIGN")
        private String callsign;

        @JsonProperty("TYPE")
        private int type;

        @JsonProperty("A")
        private int a;

        @JsonProperty("B")
        private int b;

        @JsonProperty("C")
        private int c;

        @JsonProperty("D")
        private int d;

        @JsonProperty("DRAUGHT")
        private double draught;

        @JsonProperty("DESTINATION")
        private String destination;

        @JsonProperty("LOCODE")
        private String locode;

        @JsonProperty("ETA_AIS")
        private String etaAis;

        @JsonProperty("ETA")
        private String eta;

        @JsonProperty("SRC")
        private String src;

        @JsonProperty("ZONE")
        private String zone;

        @JsonProperty("ECA")
        private boolean eca;

        @JsonProperty("DISTANCE_REMAINING")
        private Double distanceRemaining;

        @JsonProperty("ETA_PREDICTED")
        private String etaPredicted;

    }
}
