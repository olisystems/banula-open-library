package com.banula.ocpi.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@Builder
public class GeoLocationDTO {

    private String longitude;
    private String latitude;
    private final static String DEFAULT_GEOLOCATION = "Point";

    public GeoLocationDTO(double latitude, double longitude) {
        this.longitude = String.valueOf(longitude);
        this.latitude = String.valueOf(latitude);
    }

    public GeoLocationDTO(String latitude, String longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


}
