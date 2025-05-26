package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.TokenType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CdrToken {

    /**
     * Unique ID by which this Token can be identified.
     */
    @JsonProperty("uid")
    @NotEmpty(message = "uid cannot be empty.")
    @Size(max = 36, message = "uid length must be at most 36 characters.")
    private String uid;

    /**
     * Type of the token.
     */
    @JsonProperty("type")
    @NotNull(message = "token type cannot be empty.")
    private TokenType type;

    /**
     * Uniquely identifies the EV driver contract token within the eMSP’s platform.
     */
    @JsonProperty("contract_id")
    @NotEmpty(message = "contractId cannot be empty.")
    @Size(max = 36, message = "contractId length must be at most 36 characters.")
    private String contractId;

    @JsonProperty("country_code")
    @NotEmpty(message = "country_code cannot be empty.")
    @Size(max = 2, message = "country code length must be at most 2 characters.")
    private String countryCode;

    @JsonProperty("party_id")
    @NotEmpty(message = "party_id cannot be empty.")
    @Size(max = 3, message = "party_id length must be at most 3 characters.")
    private String partyId;

}
