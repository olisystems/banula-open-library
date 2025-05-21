package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.dto.GeoLocationDTO;
import com.banula.ocpi.model.enums.ConnectorFormat;
import com.banula.ocpi.model.enums.ConnectorType;
import com.banula.ocpi.model.enums.PowerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CdrLocation {

    /**
     * Uniquely identifies the location within the CPO’s platform (and suboperator platforms). This field can
     * never be changed, modified, or renamed.
     */
    @JsonProperty("id")
    @NotEmpty(message = "Id cannot be empty")
    @Size(max = 36, message = "Location ID cannot be longer than 36 characters")
    private String id;

    /**
     * Display name of the location.
     */
    @JsonProperty("name")
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
     * Postal code of the location, may only be omitted when the location has no postal code: in some countries
     * charging locations at highways don’t have postal codes.
     */
    @JsonProperty("postal_code")
    @Size(max = 10, message = "Postal code cannot be longer than 10 characters")
    private String postalCode;

    /**
     * State only to be used when relevant.
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
    private GeoLocationDTO coordinates;

    /**
     * Uniquely identifies the EVSE within the CPO’s platform (and suboperator platforms).
     * For example a database unique ID or the actual EVSE ID. This field can never be changed, modified or renamed.
     * This is the technical identification of the EVSE, not to be used as human readable identification, use the field:
     * evse_id for that. Allowed to be set to: #NA when this CDR is created for a reservation that never resulted
     * in a charging session.
     */
    @JsonProperty("evse_uid")
    @Size(max = 36, message = "EVSE UID cannot be longer than 36 characters")
    private String evseUid;

    /**
     * Compliant with the following specification for EVSE ID from "eMI3 standard version V1.0"
     * (<a href="http://emi3group.com/documents-links/">...</a>) "Part 2: business objects.".
     * Allowed to be set to: #NA when this CDR is created for a reservation that never resulted in a charging session.
     */
    @JsonProperty("evse_id")
    @Size(max = 48, message = "EVSE ID cannot be longer than 48 characters")
    private String evseId;

    /**
     * Identifier of the connector within the EVSE. Allowed to be set to: #NA when this CDR is created
     * for a reservation that never resulted in a charging session.
     */
    @JsonProperty("connector_id")
    @Size(max = 36, message = "Connector ID cannot be longer than 36 characters")
    private String connectorId;

    /**
     * The standard of the installed connector. When this CDR is created for a reservation that never resulted
     * in a charging session, this field can be set to any value and should be ignored by the Receiver.
     */
    @JsonProperty("connector_standard")
    @NotNull(message = "Connector standard cannot be null")
    private ConnectorType connectorStandard;

    /**
     * The format (socket/cable) of the installed connector. When this CDR is created for a reservation that
     * never resulted in a charging session, this field can be set to any value and should be ignored by the Receiver.
     */
    @JsonProperty("connector_format")
    @NotNull(message = "Connector format cannot be null")
    private ConnectorFormat connectorFormat;

    /**
     * When this CDR is created for a reservation that never resulted in a charging session, this field can be set
     * to any value and should be ignored by the Receiver.
     */
    @JsonProperty("connector_power_type")
    @NotNull(message = "Connector power type cannot be null")
    private PowerType connectorPowerType;
}
