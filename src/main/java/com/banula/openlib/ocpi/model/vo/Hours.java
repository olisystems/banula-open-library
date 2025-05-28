package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Opening and access hours of the location.
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Hours {

    /**
     * True to represent 24 hours a day and 7 days a week, except the given
     * exceptions.
     */
    @JsonProperty("twentyfourseven")
    @NotNull(message = "twentyFourSeven must not be null")
    private Boolean twentyFourSeven;
    /**
     * Regular hours, weekday-based. Only to be used if twentyfourseven=false, then
     * this field needs to contain at least
     * one RegularHours object.
     */
    @JsonProperty("regular_hours")
    private List<RegularHours> regularHours;
    /**
     * Exceptions for specified calendar dates, time-range based. Periods the
     * station is operating/accessible.
     * Additional to regular_hours. May overlap regular rules.
     */
    @JsonProperty("exceptional_openings")
    private List<ExceptionalPeriod> exceptionalOpenings;
    /**
     * Exceptions for specified calendar dates, time-range based. Periods the
     * station is not operating/accessible.
     * Overwriting regular_hours and exceptional_openings. Should not overlap
     * exceptional_openings.
     */
    @JsonProperty("exceptional_closings")
    private List<ExceptionalPeriod> exceptionalClosings;

}
