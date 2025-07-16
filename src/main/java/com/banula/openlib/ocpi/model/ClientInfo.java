package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ConnectionStatus;
import com.banula.openlib.ocpi.model.enums.Role;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class ClientInfo {

    /**
     * CPO or eMSP ID of this party (following the 15118 ISO standard), as used in
     * the credentials exchange.
     */
    @NotEmpty(message = "Party ID must not be empty")
    @Size(min = 1, max = 36, message = "Party ID length must be between 1 and 36 characters")
    @JsonProperty("party_id")
    private String partyId;

    /**
     * Country code of the country this party is operating in, as used in the
     * credentials exchange.
     */
    @NotEmpty(message = "Country code must not be empty")
    @Size(min = 1, max = 2, message = "Country code length must be between 1 and 2 characters")
    @JsonProperty("country_code")
    private String countryCode;

    /**
     * The role of the connected party
     */
    @NotNull(message = "Role must not be null")
    @JsonProperty("role")
    private Role role;

    /**
     * Status of the connection to the party.
     */
    @NotNull(message = "Status must not be null")
    @JsonProperty("status")
    private ConnectionStatus status;

    /**
     * Timestamp when this ClientInfo object was last updated.
     */
    @NotNull(message = "Last updated timestamp must not be null")
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;

}
