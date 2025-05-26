package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.model.enums.TokenType;
import com.banula.openlib.ocpi.model.enums.WhitelistType;
import com.banula.openlib.ocpi.model.vo.EnergyContract;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @JsonProperty("country_code")
    @Size(max = 2, message = "country_code longer than 2 characters")
    @NotEmpty(message = "country_code cannot be empty.")
    private String countryCode;

    @JsonProperty("party_id")
    @Size(max = 3, message = "party_id longer than 3 characters")
    @NotEmpty(message = "party_id cannot be empty.")
    private String partyId;

    @JsonProperty("uid")
    @Size(max = 255, message = "uid longer than 255 characters")
    @NotEmpty(message = "uid cannot be empty.")
    private String uid;

    @JsonProperty("type")
    @NotNull(message = "type cannot be empty.")
    private TokenType type;

    @JsonProperty("contract_id")
    @Size(max = 36, message = "contract_id longer than 36 characters")
    @NotEmpty(message = "contract_id cannot be empty.")
    private String contractId;

    @JsonProperty("visual_number")
    @Size(max = 64, message = "visual_number longer than 64 characters")
    private String visualNumber;

    @JsonProperty("issuer")
    @Size(max = 64, message = "issuer longer than 64 characters")
    @NotEmpty(message = "issuer cannot be empty.")
    private String issuer;

    @JsonProperty("group_id")
    @Size(max = 36, message = "group_id longer than 36 characters")
    private String groupId;

    @JsonProperty("valid")
    @NotNull(message = "valid cannot be empty.")
    private Boolean valid;

    @JsonProperty("whitelist")
    @NotNull(message = "whitelist cannot be empty.")
    private WhitelistType whitelist;

    @JsonProperty("language")
    @Size(max = 2, message = "language longer than 2 characters")
    private String language;

    @JsonProperty("default_profile_type")
    private ProfileType defaultProfileType;

    @JsonProperty("energy_contract")
    private EnergyContract energyContract;

    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;

}