package com.banula.openlib.ocpi.custom.energysuppliers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The MaLo and Mabis metering point an energy supplier uses inside one TSO control area.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergySupplierTsoEntry {

    @JsonProperty("tso_market_partner_id")
    private String tsoMarketPartnerId;

    @JsonProperty("malo_id")
    private String maloId;

    @JsonProperty("mabis_metering_point")
    private String mabisMeteringPoint;

    @JsonProperty("owner_type")
    private MaloOwnerType ownerType;
}
