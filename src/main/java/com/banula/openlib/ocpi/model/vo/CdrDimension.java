package com.banula.openlib.ocpi.model.vo;

import java.math.BigDecimal;

import com.banula.openlib.ocpi.model.enums.CdrDimensionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private BigDecimal volume;

    public void setType(CdrDimensionType type) {
        this.type = type;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

}
