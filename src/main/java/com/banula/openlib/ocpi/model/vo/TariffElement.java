package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class TariffElement {

    /**
     * List of price components that describe the pricing of a tariff.
     */
    @NotNull(message = "Price components list cannot be empty.")
    @JsonProperty("price_components")
    private List<PriceComponent> priceComponents;

    /**
     * Restrictions that describe the applicability of a tariff.
     */
    private TariffRestrictions restrictions;

    public TariffElement(List<PriceComponent> priceComponents) {
        this.priceComponents = priceComponents;
    }

    public void setPriceComponents(List<PriceComponent> priceComponents) {
        this.priceComponents = priceComponents;
    }

    public void setRestrictions(TariffRestrictions restrictions) {
        this.restrictions = restrictions;
    }
}
