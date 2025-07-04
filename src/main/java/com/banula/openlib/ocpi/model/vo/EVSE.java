package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.dto.GeoLocationDTO;
import com.banula.openlib.ocpi.model.enums.Capability;
import com.banula.openlib.ocpi.model.enums.ParkingRestriction;
import com.banula.openlib.ocpi.model.enums.Status;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EVSE {

    /**
     * Uniquely identifies the EVSE within the CPOs platform (and suboperator
     * platforms). For example a
     * database ID or the actual "EVSE ID". This field can never be changed,
     * modified or renamed.
     * This is the 'technical' identification of the EVSE, not to be used as 'human
     * readable' identification,
     * use the field evse_id for that. This field is named uid instead of id,
     * because id could be confused
     * with evse_id which is an eMI3 defined field.
     */
    @JsonProperty("uid")
    @NotNull(message = "UID cannot be null")
    @Size(max = 36, message = "UID cannot be longer than 36 characters")
    private String uid;

    /**
     * Compliant with the following specification for EVSE ID from "eMI3 standard
     * version V1.0"
     * (<a href="http://emi3group.com/documents-links/">EMI3 reference</a>) "Part 2: business objects." Optional
     * because: if an evse_id is
     * to be re-used in the real world, the evse_id can be removed from an EVSE
     * object if the status is set to REMOVED.
     */
    @JsonProperty("evse_id")
    @Size(max = 48, message = "EVSE ID cannot be longer than 48 characters")
    @NotEmpty(message = "EvseId is a required field in Banula style of Charging")
    private String evseId;

    /**
     * Indicates the current status of the EVSE.
     */
    @JsonProperty("status")
    @NotNull(message = "Status cannot be null")
    private Status status;

    /**
     * Indicates a planned status update of the EVSE.
     */
    @JsonProperty("status_schedule")
    private List<StatusSchedule> status_schedule;

    /**
     * List of functionalities that the EVSE is capable of.
     */
    @JsonProperty("capabilities")
    private List<Capability> capabilities;

    /**
     * List of available connectors on the EVSE.
     */
    @JsonProperty("connectors")
    @NotNull(message = "Connectors cannot be empty")
    @Valid
    private List<Connector> connectors;

    /**
     * Level on which the Charge Point is located (in garage buildings) in the
     * locally displayed numbering scheme.
     */
    @JsonProperty("floor_level")
    @Size(max = 16, message = "Floor level cannot be longer than 16 characters")
    private String floorLevel;

    /**
     * Coordinates of the EVSE.
     */
    @JsonProperty("coordinates")
    @Valid
    private GeoLocationDTO coordinates;

    /**
     * A number/string printed on the outside of the EVSE for visual
     * identification.
     */
    @JsonProperty("physical_reference")
    @Size(max = 16, message = "Physical reference cannot be longer than 16 characters")
    private String physicalReference;

    /**
     * Multi-language human-readable directions when more detailed information on
     * how to reach the EVSE from
     * the Location is required.
     */
    @JsonProperty("directions")
    private List<DisplayText> directions;

    /**
     * The restrictions that apply to the parking spot.
     */
    @JsonProperty("parking_restrictions")
    private List<ParkingRestriction> parkingRestrictions;

    /**
     * Links to images related to the EVSE such as photos or logos.
     */
    @JsonProperty("images")
    private List<Image> images;

    /**
     * Timestamp when this EVSE or one of its Connectors was last updated (or
     * created).
     */
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Last updated timestamp cannot be null")
    private LocalDateTime lastUpdated;
}
