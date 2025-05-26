package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Regular recurring operation or access hours.
 */
@Data
@ToString
@NoArgsConstructor
public class RegularHours {

    /**
     * Number of day in the week, from Monday (1) till Sunday (7)
     */
    @JsonProperty("weekday")
    @NotNull(message = "weekday must not be null")
    private Integer weekday;

    /**
     * Begin of the regular period, in local time, given in hours and minutes. Must
     * be in 24h format with leading zeros.
     * Example: "18:15". Hour/Minute separator: ":" Regex:
     * ([0-1][0-9]|2[0-3]):[0-5][0-9].
     */
    @JsonProperty("period_begin")
    @NotEmpty(message = "period_begin must not be null")
    private String periodBegin;

    /**
     * End of the regular period, in local time, syntax as for period_begin. Must be
     * later than period_begin.
     */
    @JsonProperty("period_end")
    @NotEmpty(message = "period_end must not be null")
    private String periodEnd;
}
