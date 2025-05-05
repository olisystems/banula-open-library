package com.banula.ocpi.model.vo;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoLocation {

    private String type;
    private List<Double> coordinates;
    private final static String DEFAULT_GEOLOCATION = "Point";

    public GeoLocation(double latitude, double longitude) {
        this.type = DEFAULT_GEOLOCATION;
        this.coordinates = Arrays.asList(latitude, longitude);
    }

    public GeoLocation(String latitude, String longitude) {
        this.type = DEFAULT_GEOLOCATION;
        this.coordinates = Arrays.asList(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

}
