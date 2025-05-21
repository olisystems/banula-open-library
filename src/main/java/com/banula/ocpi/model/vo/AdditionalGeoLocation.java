package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class defines an additional geo location that is relevant for the Charge Point.
 * The geodetic system to be used is WGS 84.
 */
@Data
@ToString
@NoArgsConstructor
public class AdditionalGeoLocation {

    /**
     * Latitude of the point in decimal degree. Example: 50.770774. Decimal separator: "."
     * Regex: -?[0-9]{1,2}\.[0-9]{5,7}
     */
    @JsonProperty("latitude")
    @NotEmpty(message = "Latitude cannot be empty")
    @Size(max = 10, message = "Latitude cannot be longer than 10 characters")
    private String latitude;

    /**
     * Longitude of the point in decimal degree. Example: -126.104965. Decimal separator: "."
     * Regex: -?[0-9]{1,3}\.[0-9]{5,7}
     */
    @JsonProperty("longitude")
    @NotEmpty(message = "Longitude cannot be empty")
    @Size(max = 11, message = "Longitude cannot be longer than 11 characters")
    private String longitude;

    @JsonProperty("name")
    private DisplayText name;
}
