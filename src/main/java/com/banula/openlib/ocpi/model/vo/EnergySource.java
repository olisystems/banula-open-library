package com.banula.openlib.ocpi.model.vo;

import com.banula.openlib.ocpi.model.enums.EnergySourceCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnergySource {

    /**
     * The type of energy source.
     */
    @JsonProperty("source")
    @NotNull(message = "Energy source category cannot be null.")
    private EnergySourceCategory source;

    /**
     * Percentage of this source (0-100) in the mix.
     */
    @JsonProperty("percentage")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Percentage should be a valid number.")
    @NotNull(message = "Percentage cannot be null.")
    private Float percentage;

}
