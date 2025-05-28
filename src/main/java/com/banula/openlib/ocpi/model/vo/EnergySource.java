package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.EnergySourceCategory;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
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
