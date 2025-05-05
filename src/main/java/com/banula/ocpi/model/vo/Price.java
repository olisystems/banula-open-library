package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Price {

    public Price(float exclVat) {
        this.exclVat = exclVat;
    }

    public Price(float exclVat, float inclVat) {
        this.exclVat = exclVat;
        this.inclVat = inclVat;
    }

    /**
     * Price/Cost excluding VAT.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Invalid price format for exclVat.")
    @NotNull(message = "ExclVat cannot be null.")
    @JsonProperty("excl_vat")
    private Float exclVat;

    /**
     * Price/Cost including VAT.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Invalid price format for inclVat.")
    @JsonProperty("incl_vat")
    private Float inclVat;

}
