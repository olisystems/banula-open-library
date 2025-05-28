package com.banula.openlib.ocn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignableHeaders {
    @JsonProperty("x-correlation-id")
    public String correlationId;

    @JsonProperty("ocpi-from-country-code")
    public String fromCountryCode;

    @JsonProperty("ocpi-from-party-id")
    public String fromPartyId;

    @JsonProperty("ocpi-to-country-code")
    public String toCountryCode;

    @JsonProperty("ocpi-to-party-id")
    public String toPartyId;

    @JsonProperty("x-limit")
    public String limit;

    @JsonProperty("x-total-count")
    public String totalCount;

    @JsonProperty("link")
    public String link;

    @JsonProperty("location")
    public String location;
}