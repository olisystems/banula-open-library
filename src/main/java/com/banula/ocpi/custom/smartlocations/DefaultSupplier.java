package com.banula.ocpi.custom.smartlocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSupplier {
    @JsonProperty("supplier_market_partner_id")
    @NotEmpty(message = "Supplier market partner ID cannot be empty")
    private String supplierMarketPartnerId;

    @JsonProperty("bkv_id")
    @NotEmpty(message = "BKV ID cannot be empty")
    private String bkvId;

    @JsonProperty("balancing_group_eic_id")
    @NotEmpty(message = "Balancing group EIC ID cannot be empty")
    private String balancingGroupEicId;
}
