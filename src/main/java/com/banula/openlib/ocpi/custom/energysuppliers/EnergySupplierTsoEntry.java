package com.banula.openlib.ocpi.custom.energysuppliers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The MaLo and Mabis metering points an energy supplier uses inside one TSO control area: the balancing
 * MaBiS-ZP ({@code mabis_metering_point}) and, optionally, the Supplier MaBiS-ZP under which the supplier
 * receives its own monthly time series for the control area ({@code supplier_mabis_metering_point}).
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

    @JsonProperty("supplier_mabis_metering_point")
    private String supplierMabisMeteringPoint;

    @JsonProperty("owner_type")
    private MaloOwnerType ownerType;
}
