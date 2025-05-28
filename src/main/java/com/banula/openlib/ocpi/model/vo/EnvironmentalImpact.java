package com.banula.openlib.ocpi.model.vo;

import com.banula.openlib.ocpi.model.enums.EnvironmentalImpactCategory;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EnvironmentalImpact {

    /**
     * The environmental impact category of this value.
     */
    private EnvironmentalImpactCategory category;

    /**
     * Amount of this portion in g/kWh.
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Amount should be a valid number.")
    @NotNull(message = "Amount cannot be null.")
    private Float amount;

    public EnvironmentalImpact(EnvironmentalImpactCategory category, Float amount) {
        this.category = category;
        this.amount = amount;
    }

    public void setCategory(EnvironmentalImpactCategory category) {
        this.category = category;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
