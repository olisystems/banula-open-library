package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.TariffDimensionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PriceComponent {

    /**
     * Type of tariff dimension.
     */
    @NotNull
    @JsonProperty("type")
    private TariffDimensionType type;

    /**
     * Price per unit (excl. VAT) for this tariff dimension.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    @NotNull
    @JsonProperty("price")
    private Float price;

    /**
     * Applicable VAT percentage for this tariff dimension. If omitted, no VAT is
     * applicable.
     * Not providing a VAT is different from 0% VAT, which would be a value of 0.0
     * here.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    @JsonProperty("vat")
    private Float vat;

    /**
     * Minimum amount to be billed. This unit will be billed in this step_size
     * blocks.
     * Amounts that are less than this step_size are rounded up to the given
     * step_size.
     * For example: if type is TIME and step_size has a value of 300, then time will
     * be billed in blocks of 5 minutes.
     * If 6 minutes were used, 10 minutes (2 blocks of step_size) will be billed.
     */
    @NotNull
    @JsonProperty("step_size")
    private Integer stepSize;

    public PriceComponent(TariffDimensionType type, Float price, Integer stepSize) {
        this.type = type;
        this.price = price;
        this.stepSize = stepSize;
    }

    public PriceComponent(TariffDimensionType type, Float price, Integer stepSize, Float vat) {
        this.type = type;
        this.price = price;
        this.stepSize = stepSize;
        this.vat = vat;
    }
}
