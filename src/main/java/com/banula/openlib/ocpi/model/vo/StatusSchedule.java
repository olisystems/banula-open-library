package com.banula.openlib.ocpi.model.vo;

import java.time.LocalDateTime;

import com.banula.openlib.ocpi.model.enums.Status;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
