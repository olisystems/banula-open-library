package com.banula.openlib.ocpi.custom.energysuppliers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * An energy supplier (ES) onboarded once for the whole network. It is registered in the Hub
 * (platform-banula) and mirrored by the CDR Adapter. The key is the ES market partner ID, the value
 * tokens carry in {@code energy_contract.supplier_name}; {@link #perTso} holds the MaLo the ES uses in
 * each TSO control area.
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergySupplier {

    public static final int MAX_TSO_ENTRIES = 4;

    @Id
    @JsonProperty("es_market_partner_id")
    private String esMarketPartnerId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("bkv_market_partner_id")
    private String bkvMarketPartnerId;

    /** Bilanzkreis, an EIC code. */
    @JsonProperty("balancing_group_id")
    private String balancingGroupId;

    @JsonProperty("bkze_reference")
    private String bkzeReference;

    @JsonProperty("status")
    private EnergySupplierStatus status;

    @Builder.Default
    @JsonProperty("per_tso")
    private List<EnergySupplierTsoEntry> perTso = new ArrayList<>();

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    /** Only set on the CDR Adapter mirror: when the entry was last written by a sync from the Hub. */
    @JsonProperty("synced_at")
    private Instant syncedAt;

    /** The entry for one TSO control area, e.g. the one a smart location belongs to. */
    @JsonIgnore
    public Optional<EnergySupplierTsoEntry> findTsoEntry(String tsoMarketPartnerId) {
        if (perTso == null || tsoMarketPartnerId == null) {
            return Optional.empty();
        }
        return perTso.stream()
                .filter(entry -> entry != null && tsoMarketPartnerId.equals(entry.getTsoMarketPartnerId()))
                .findFirst();
    }
}
