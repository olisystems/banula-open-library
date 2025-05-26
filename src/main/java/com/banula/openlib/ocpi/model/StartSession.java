package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StartSession {

    /**
     * Token object the Charge Point has to use to start a new session. The Token
     * provided in this request
     * is authorized by the eMSP.
     */
    @JsonProperty("token")
    @NotNull(message = "token cannot be empty.")
    @Valid
    private Token token;

    /**
     * Location.id of the Location (belonging to the CPO this request is send to) on
     * which a session is to be started.
     */
    @JsonProperty("location_id")
    @Size(max = 36, message = "Location id longer than 36 characters")
    @NotEmpty(message = "locationId cannot be empty.")
    private String locationId;

    /**
     * Optional EVSE.uid of the EVSE of this Location on which a session is to be
     * started.
     * Required when connector_id is set.
     */
    @JsonProperty("evse_uid")
    @Size(max = 36, message = "EvseUid longer than 36 characters")
    private String evseUid;

    /*
     * Optional Connector.id of the Connector of the EVSE on which a session is to
     * be started.
     * This field is required when the capability: START_SESSION_CONNECTOR_REQUIRED
     * is set on the EVSE.
     */
    @JsonProperty("connector_id")
    @Size(max = 36, message = "ConnectorId longer than 36 characters")
    private String connectorId;

    /**
     * Reference to the authorization given by the eMSP, when given, this reference
     * will be provided
     * in the relevant Session and/or CDR.
     */
    @JsonProperty("authorization_reference")
    @Size(max = 36, message = "authorizationReference longer than 36 characters")
    private String authorizationReference;

    @NotNull(message = "response_url cannot be empty.")
    @JsonProperty("response_url")
    private String responseUrl;

}
