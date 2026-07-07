package com.banula.openlib.ocpi.model.vo;

import java.time.LocalDateTime;

import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Specifies one exceptional period for opening or access hours.
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
