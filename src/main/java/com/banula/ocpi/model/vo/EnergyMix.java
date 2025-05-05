package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.AvailableFlexibility;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.beans.Transient;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EnergyMix {

    /**
     * True if 100% from regenerative sources. (CO2 and nuclear waste is zero)
     */
    @JsonProperty("is_green_energy")
    @NotNull(message = "greenEnergy cannot be null")
    private boolean greenEnergy;

    /**
     * Key-value pairs (enum + percentage) of energy sources of this location’s tariff.
     */
    @JsonProperty("energy_sources")
    private List<EnergySource> energySources;

    /**
     * Key-value pairs (enum + percentage) of nuclear waste and CO2 exhaust of this location’s tariff.
     */
    @JsonProperty("environ_impact")
    private List<EnvironmentalImpact> environImpact;

    /**
     * Name of the energy supplier, delivering the energy for this location or tariff.
     */
    @Size(max = 64)
    @JsonProperty("supplier_name")
    private String supplierName;

    /**
     * Name of the energy suppliers product/tariff plan used at this location.
     */
    @Size(max = 64)
    @JsonProperty("energy_product_name")
    private String energyProductName;

    public void setAvailableFlexibility(AvailableFlexibility availableFlexibility) {
        this.energyProductName = availableFlexibility.getValue();
    }

    @Transient
    public AvailableFlexibility getAvailableFlexibility() {
        return AvailableFlexibility.fromValue(energyProductName);
    }

    public EnergyMix(boolean greenEnergy) {
        this.greenEnergy = greenEnergy;
    }

}
