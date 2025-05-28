package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Charging profile period structure defines a time period in a charging profile, as used in: ChargingProfile
 */
@Data
@ToString
@NoArgsConstructor
public class ChargingProfilePeriod {

    /**
     * Start of the period, in seconds from the start of profile.
     * The value of StartPeriod also defines the stop time of the previous period.
     */
    @JsonProperty("start_period")
    @NotNull
    private Integer startPeriod;

    /**
     * Charging rate limit during the profile period, in the applicable chargingRateUnit, for example in Amperes (A)
     * or Watts (W). Accepts at most one digit fraction (e.g. 8.1).
     */
    @JsonProperty("limit")
    @NotNull
    private Float limit;

    public void setStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
    }

    public void setLimit(Float limit) {
        this.limit = limit;
    }
}
