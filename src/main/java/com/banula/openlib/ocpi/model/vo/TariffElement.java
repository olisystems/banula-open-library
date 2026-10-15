package com.banula.openlib.ocpi.model.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TariffElement {

    /**
     * List of price components that describe the pricing of a tariff.
     */
    @NotNull(message = "Price components list cannot be empty.")
    @Valid
    @JsonProperty("price_components")
    private List<PriceComponent> priceComponents;

    /**
     * Restrictions that describe the applicability of a tariff.
     */
    @Valid
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
