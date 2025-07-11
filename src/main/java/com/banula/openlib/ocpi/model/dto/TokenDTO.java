package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.model.enums.TokenType;
import com.banula.openlib.ocpi.model.enums.WhitelistType;
import com.banula.openlib.ocpi.model.vo.EnergyContract;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {
    @NotEmpty(message = "Country code must not be blank")
    @Size(min = 1, max = 2, message = "Country code must be between 1 and 2 characters")
    @JsonProperty("country_code")
    private String countryCode;

    @NotEmpty(message = "Party ID must not be blank")
    @Size(min = 1, max = 3, message = "Party ID must be between 1 and 3 characters")
    @JsonProperty("party_id")
    private String partyId;

    @NotEmpty(message = "UID must not be blank")
    @Size(min = 1, max = 36, message = "UID must be between 1 and 36 characters")
    @JsonProperty("uid")
    private String uid;

    @NotNull(message = "Token type must not be blank")
    @JsonProperty("type")
    private TokenType type;

    @NotEmpty(message = "Contract ID must not be blank")
    @Size(min = 1, max = 36, message = "Contract ID must be between 1 and 36 characters")
    @JsonProperty("contract_id")
    private String contractId;

    @Size(min = 1, max = 64, message = "Visual number must be between 1 and 64 characters")
    @JsonProperty("visual_number")
    private String visualNumber;

    @NotEmpty(message = "Issuer must not be blank")
    @Size(min = 1, max = 64, message = "Issuer must be between 1 and 64 characters")
    @JsonProperty("issuer")
    private String issuer;

    @Size(min = 1, max = 36, message = "Group ID must be between 1 and 36 characters")
    @JsonProperty("group_id")
    private String groupId;

    @NotNull(message = "Valid status must not be blank")
    @JsonProperty("valid")
    private Boolean valid;

    @NotNull(message = "Whitelist type must not be blank")
    @JsonProperty("whitelist")
    private WhitelistType whitelist;

    @Size(min = 1, max = 2, message = "Language must be between 1 and 2 characters")
    @JsonProperty("language")
    private String language;

    @JsonProperty("default_profile_type")
    private ProfileType defaultProfileType;

    @JsonProperty("energy_contract")
    @Valid
    @NotNull(message = "energy_contract is a required field in Banula style of charging")
    private EnergyContract energyContract;

    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull
    private LocalDateTime lastUpdated;
}
