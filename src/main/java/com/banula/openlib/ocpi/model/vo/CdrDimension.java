package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.CdrDimensionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CdrDimension {

    /**
     * Type of CDR dimension.
     */
    @JsonProperty("type")
    @NotNull(message = "Type cannot be empty")
    private CdrDimensionType type;

    /**
     * Volume of the dimension consumed, measured according to the dimension type.
     */
    @JsonProperty("volume")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float volume;

    public void setType(CdrDimensionType type) {
        this.type = type;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

}
