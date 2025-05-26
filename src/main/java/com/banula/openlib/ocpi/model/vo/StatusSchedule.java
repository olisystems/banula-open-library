package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.Status;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This type is used to schedule status periods in the future. The eMSP can
 * provide this information to the EV user
 * for trip planning purposes. A period MAY have no end. Example: "This station
 * will be running as of tomorrow.
 * Today it is still planned and under construction."
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class StatusSchedule {

    /**
     * Begin of the scheduled period.
     */
    @JsonProperty("period_begin")
    @NotNull(message = "Period begin cannot be null")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime periodBegin;

    /**
     * End of the scheduled period, if known.
     */
    @JsonProperty("period_end")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime periodEnd;

    /**
     * Status value during the scheduled period.
     */
    @NotNull(message = "Status cannot be null")
    private Status status;

}
