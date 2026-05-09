package com.banula.openlib.ocn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicRole {
    @JsonProperty("party_id")
    private String partyId;
    
    @JsonProperty("country_code")
    private String countryCode;
}
