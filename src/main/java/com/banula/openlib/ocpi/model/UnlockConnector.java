package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UnlockConnector {

    /**
     * Location.id of the Location (belonging to the CPO this request is sent to) of
     * which it is requested
     * to unlock the connector.
     */
    @JsonProperty("location_id")
    @NotEmpty(message = "Location ID cannot be empty.")
    private String locationId;

    /**
     * EVSE.uid of the EVSE of this Location of which it is requested to unlock the
     * connector.
     */
    @JsonProperty("evse_uid")
    @NotEmpty(message = "EVSE UID cannot be empty.")
    private String evseUid;

    /**
     * Connector.id of the Connector of this Location of which it is requested to
     * unlock.
     */
    @JsonProperty("connector_id")
    @NotEmpty(message = "Connector ID cannot be empty.")
    private String connectorId;

    @NotEmpty(message = "Response URL cannot be empty.")
    @JsonProperty("response_url")
    private String responseUrl;

}
