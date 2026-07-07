package com.banula.openlib.ocpi.model.vo;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
