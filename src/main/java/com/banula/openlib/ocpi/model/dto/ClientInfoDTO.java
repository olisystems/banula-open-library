package com.banula.openlib.ocpi.model.dto;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDTO {

    @NotEmpty(message = "Party ID must not be blank")
    @Size(min = 1, max = 3, message = "Party ID must be between 1 and 3 characters")
    @JsonProperty("party_id")
    private String partyId;

    @NotEmpty(message = "Country code must not be blank")
    @Size(min = 1, max = 2, message = "Country code must be between 1 and 2 characters")
    @JsonProperty("country_code")
    private String countryCode;

    @NotNull(message = "Role must not be blank")
    @JsonProperty("role")
    private Role role;

    @NotNull(message = "Status must not be blank")
    @JsonProperty("status")
    private ConnectionStatus status;

    @NotNull(message = "Last updated must not be blank")
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;

}