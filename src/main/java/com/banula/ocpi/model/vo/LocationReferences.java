package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * References to location details.
 */
@Data
@ToString
@NoArgsConstructor
public class LocationReferences {

    /**
     * Unique identifier for the location.
     */
    @NotNull(message = "Location ID must not be null")
    @Size(min = 1, max = 36, message = "Location ID length must be between 1 and 36 characters")
    @JsonProperty("location_id")
    private String locationId;

    /**
     * Unique identifiers for EVSEs within the CPO’s platform for the EVSE within the given location.
     */
    @NotNull(message = "EVSE UIDs must not be empty")
    @JsonProperty("evse_uids")
    private List<String> evseUids;

}
