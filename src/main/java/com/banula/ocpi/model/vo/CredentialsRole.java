package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsRole {

    /**
     * Type of role.
     */
    @NotNull(message = "Role must not be null")
    @JsonProperty("role")
    private Role role;

    /**
     * CPO, eMSP (or other role) ID of this party (following the ISO-15118 standard).
     */
    @NotEmpty(message = "Party ID must not be empty")
    @Size(max = 3, message = "Party ID length must be at most 3 characters")
    @JsonProperty("party_id")
    private String partyId;

    /**
     * ISO-3166 alpha-2 country code of the country this party is operating in.
     */
    @NotEmpty(message = "Country code must not be empty")
    @Size(min = 1, max = 2, message = "Country code length must be between 1 and 2 characters")
    @JsonProperty("country_code")
    private String countryCode;

    /**
     * Details of this party.
     */
    @NotNull(message = "Business details must not be null")
    @JsonProperty("business_details")
    private BusinessDetails businessDetails;

}
