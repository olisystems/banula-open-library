package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChargingPeriod {

    /**
     * Start timestamp of the charging period. A period ends when the next period
     * starts.
     * The last period ends when the session ends.
     */
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Start date time is required")
    private LocalDateTime startDateTime;

    /**
     * List of relevant values for this charging period.
     */
    @JsonProperty("dimensions")
    @NotNull(message = "Dimensions list cannot be empty")
    private List<CdrDimension> dimensions;

    /**
     * Unique identifier of the Tariff that is relevant for this Charging Period. If
     * not provided,
     * no Tariff is relevant during this period.
     */
    @JsonProperty("tariff_id")
    private String tariffId;

}
