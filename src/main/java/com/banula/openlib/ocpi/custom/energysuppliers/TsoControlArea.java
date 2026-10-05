package com.banula.openlib.ocpi.custom.energysuppliers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One of the German TSO control areas (Regelzonen) an energy supplier can register a MaLo for.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TsoControlArea {

    @JsonProperty("name")
    private String name;

    @JsonProperty("tso_market_partner_id")
    private String tsoMarketPartnerId;

    @JsonProperty("biko_market_partner_id")
    private String bikoMarketPartnerId;

    @JsonProperty("mabis_metering_point")
    private String mabisMeteringPoint;
}
