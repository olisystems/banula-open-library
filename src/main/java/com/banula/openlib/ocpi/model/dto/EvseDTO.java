package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.Capability;
import com.banula.openlib.ocpi.model.enums.ParkingRestriction;
import com.banula.openlib.ocpi.model.enums.Status;
import com.banula.openlib.ocpi.model.vo.DisplayText;
import com.banula.openlib.ocpi.model.vo.Image;
import com.banula.openlib.ocpi.model.vo.StatusSchedule;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvseDTO {

    @NotNull(message = "UID cannot be null")
    @Size(max = 36, message = "UID cannot be longer than 36 characters")
    @JsonProperty("uid")
    private String uid;

    @Size(max = 48, message = "EVSE ID cannot be longer than 48 characters")
    @NotEmpty(message = "EvseId is a required field in Banula style of Charging")
    @JsonProperty("evse_id")
    private String evseId;

    @NotNull(message = "Status cannot be null")
    @JsonProperty("status")
    private Status status;

    @JsonProperty("status_schedule")
    private List<StatusSchedule> statusSchedule;

    @JsonProperty("capabilities")
    private List<Capability> capabilities;

    @NotNull(message = "Connectors cannot be empty")
    @Valid
    @JsonProperty("connectors")
    private List<ConnectorDTO> connectors;

    @Size(max = 16, message = "Floor level cannot be longer than 16 characters")
    @JsonProperty("floor_level")
    private String floorLevel;

    @Valid
    @JsonProperty("coordinates")
    private GeoLocationDTO coordinates;

    @Size(max = 16, message = "Physical reference cannot be longer than 16 characters")
    @JsonProperty("physical_reference")
    private String physicalReference;

    @JsonProperty("directions")
    private List<DisplayText> directions;

    @JsonProperty("parking_restrictions")
    private List<ParkingRestriction> parkingRestrictions;

    @JsonProperty("images")
    private List<Image> images;

    @NotNull(message = "Last updated timestamp cannot be null")
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
