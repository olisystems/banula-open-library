package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.model.enums.Facility;
import com.banula.ocpi.model.enums.ParkingType;
import com.banula.ocpi.model.vo.*;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The Location object describes the location and its properties where a group of EVSEs that belong
 * together are installed. Typically, the Location object is the exact location of the group of EVSEs,
 * but it can also be the entrance of a parking garage which contains these EVSEs.
 * The exact way to reach each EVSE can be further specified by its own properties.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Location {

    /**
     * ISO-3166 alpha-2 country code of the CPO that 'owns' this Location.
     */
    @JsonProperty("country_code")
    @NotEmpty(message = "Country code cannot be empty")
    @Size(max = 2, message = "Country code must be exactly 2 characters")
    private String countryCode;

    /**
     * ID of the CPO that 'owns' this Location (following the ISO-15118 standard).
     */
    @JsonProperty("party_id")
    @NotEmpty(message = "Party ID cannot be empty")
    @Size(max = 3, message = "Party ID cannot be longer than 3 characters")
    private String partyId;

    /**
     * Uniquely identifies the location within the CPOs platform (and suboperator platforms).
     * This field can never be changed, modified or renamed.
     */
    @JsonProperty("id")
    @NotEmpty(message = "Location ID cannot be empty")
    @Size(max = 36, message = "Location ID cannot be longer than 36 characters")
    private String id;

    /**
     * Defines if a Location may be published on an website or app etc. When this is set to false, only tokens
     * identified in the field: publish_allowed_to are allowed to be shown this Location.
     * When the same location has EVSEs that may be published and may not be published, two 'Locations' should be created.
     */
    @JsonProperty("publish")
    private Boolean publish;

    /**
     * This field may only be used when the publish field is set to false. Only owners of Tokens that match all the set
     * fields of one PublishToken in the list are allowed to be shown this location.
     */
    @JsonProperty("publish_allowed_to")
    private List<PublishTokenType> publishAllowedTo;

    /**
     * Display name of the location.
     */
    @JsonProperty("name")
    @NotEmpty(message = "Location name cannot be empty")
    @Size(max = 255, message = "Location name cannot be longer than 255 characters")
    private String name;

    /**
     * Street/block name and house number if available.
     */
    @JsonProperty("address")
    @NotEmpty(message = "Address cannot be empty")
    @Size(max = 45, message = "Address cannot be longer than 45 characters")
    private String address;

    /**
     * City or town.
     */
    @JsonProperty("city")
    @NotEmpty(message = "City cannot be empty")
    @Size(max = 45, message = "City cannot be longer than 45 characters")
    private String city;

    /**
     * Postal code of the location, may only be omitted when the location has no postal code: in some countries charging
     * locations at highways don’t have postal codes.
     */
    @JsonProperty("postal_code")
    @Size(max = 10, message = "Postal code cannot be longer than 10 characters")
    private String postalCode;

    /**
     * State or province of the location, only to be used when relevant.
     */
    @JsonProperty("state")
    @Size(max = 20, message = "State cannot be longer than 20 characters")
    private String state;

    /**
     * ISO 3166-1 alpha-3 code for the country of this location.
     */
    @JsonProperty("country")
    @NotEmpty(message = "Country cannot be empty")
    @Size(max = 3, message = "Country code must be exactly 3 characters")
    private String country;

    /**
     * Coordinates of the location.
     */
    @JsonProperty("coordinates")
    @NotNull(message = "Coordinates cannot be null")
    @Valid
    private GeoLocation coordinates;

    /**
     * Geographical location of related points relevant to the user.
     */
    @JsonProperty("related_locations")
    private List<AdditionalGeoLocation> relatedLocations;

    /**
     * The general type of parking at the charge point location.
     */
    @JsonProperty("parking_type")
    private ParkingType parkingType;

    /**
     * List of EVSEs that belong to this Location.
     */
    @JsonProperty("evses")
    private List<EVSE> evses;

    /**
     * Human-readable directions on how to reach the location.
     */
    @JsonProperty("directions")
    private List<DisplayText> directions;

    /**
     * Information of the operator. When not specified, the information retrieved from the Credentials module,
     * selected by the country_code and party_id of this Location, should be used instead.
     */
    @JsonProperty("operator")
    @Valid
    private BusinessDetails operator;

    /**
     * Information of the suboperator if available.
     */
    @JsonProperty("suboperator")
    @Valid
    private BusinessDetails subOperator;

    /**
     * Information of the owner if available.
     */
    @JsonProperty("owner")
    @Valid
    private BusinessDetails owner;

    /**
     * Optional list of facilities this charging location directly belongs to.
     */
    @JsonProperty("facilities")
    private List<Facility> facilities;

    /**
     * One of IANA tzdata’s TZ-values representing the time zone of the location.
     * Examples: "Europe/Oslo", "Europe/Zurich". (http://www.iana.org/time-zones)
     */
    @JsonProperty("time_zone")
    @NotEmpty(message = "Time zone cannot be empty")
    @Size(max = 255, message = "Time zone cannot be longer than 255 characters")
    private String timeZone;

    /**
     * The times when the EVSEs at the location can be accessed for charging.
     */
    @JsonProperty("opening_times")
    @Valid
    private Hours openingTimes;

    /**
     * Indicates if the EVSEs are still charging outside the opening hours of the location.
     * E.g. when the parking garage closes its barriers over night, is it allowed to charge till the next morning?
     * Default: true
     */
    @JsonProperty("charging_when_closed")
    private Boolean chargingWhenClosed = true;

    /**
     * Links to images related to the location such as photos or logos.
     */
    @JsonProperty("images")
    private List<Image> images;

    /**
     * Details on the energy supplied at this location.
     */
    @JsonProperty("energy_mix")
    @Valid
    private EnergyMix energyMix;

    /**
     * Timestamp when this Location or one of its EVSEs or Connectors were last updated (or created).
     */
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Last updated time cannot be null")
    private LocalDateTime lastUpdated;
}
