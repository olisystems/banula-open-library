package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Specifies one exceptional period for opening or access hours.
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ExceptionalPeriod {

    /**
     * Begin of the exception. In UTC, time_zone field can be used to convert to
     * local time.
     */
    @JsonProperty("period_begin")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "periodBegin must not be null")
    private LocalDateTime periodBegin;

    /**
     * End of the exception. In UTC, time_zone field can be used to convert to local
     * time.
     */
    @JsonProperty("period_end")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "periodEnd must not be null")
    private LocalDateTime periodEnd;
}
