package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.Facility;
import com.banula.openlib.ocpi.model.enums.ParkingType;
import com.banula.openlib.ocpi.model.vo.*;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {

    private static final Set<String> ISO_ALPHA3 =
            Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);

    @NotEmpty(message = "Country code must not be blank")
    @Size(min = 1, max = 2, message = "Country code must be between 1 and 2 characters")
    @JsonProperty("country_code")
    private String countryCode;

    @NotEmpty(message = "Party ID must not be blank")
    @Size(min = 1, max = 3, message = "Party ID must be between 1 and 3 characters")
    @JsonProperty("party_id")
    private String partyId;

    @NotEmpty(message = "ID must not be blank")
    @Size(min = 1, max = 36, message = "ID must be between 1 and 36 characters")
    private String id;

    @NotNull(message = "Publish status must not be null")
    private Boolean publish;

    @JsonProperty("publish_allowed_to")
    private List<PublishTokenType> publishAllowedTo;

    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Size(min = 1, max = 45, message = "Address must be between 1 and 45 characters")
    @NotEmpty
    private String address;

    @Size(min = 1, max = 45, message = "City must be between 1 and 45 characters")
    @NotEmpty
    private String city;

    @Size(min = 1, max = 10, message = "Postal code must be between 1 and 10 characters")
    @JsonProperty("postal_code")
    private String postalCode;

    @Size(min = 1, max = 20, message = "State must be between 1 and 20 characters")
    private String state;


    @NotNull
    @Pattern(regexp = "^[A-Z]{3}$",
            message = "country must be 3 upper-case letters (ISO 3166-1 alpha-3)")
    private String country;

    // in case of future queying by this field using newsphere requests (mongodb) it
    // should be changed to GeoLocation
    @NotNull(message = "Coordinates must not be null")
    @Valid
    private GeoLocationDTO coordinates;

    @JsonProperty("related_locations")
    private List<AdditionalGeoLocation> relatedLocations;

    @JsonProperty("parking_type")
    private ParkingType parkingType;

    private List<EVSE> evses;

    private List<DisplayText> directions;

    private BusinessDetails operator;

    @JsonProperty("suboperator")
    private BusinessDetails subOperator;

    private BusinessDetails owner;

    private List<Facility> facilities;

    @NotNull(message = "Time zone must not be null")
    @Size(min = 1, max = 255, message = "Time zone must be between 1 and 255 characters")
    @JsonProperty("time_zone")
    private String timeZone;

    @JsonProperty("opening_times")
    private Hours openingTimes;

    @JsonProperty("charging_when_closed")
    private Boolean chargingWhenClosed;

    private List<Image> images;

    @JsonProperty("energy_mix")
    private EnergyMix energyMix;

    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull
    private LocalDateTime lastUpdated;

    @AssertTrue(message = "country is not a valid ISO 3166-1 alpha-3 value")
    private boolean isCountryFieldValid() {
        if (country == null) { // letting @NotNull handle nullability
            return true;
        }
        return ISO_ALPHA3.contains(country);
    }

}
