package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class ActiveChargingProfile {

    /**
     * Date and time at which the Charge Point has calculated this
     * ActiveChargingProfile.
     * All time measurements within the profile are relative to this timestamp.
     */
    @NotNull(message = "Start date time must not be null")
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    /**
     * Charging profile structure defines a list of charging periods.
     */
    @Valid
    @NotNull(message = "Charging profile must not be null")
    @JsonProperty("charging_profile")
    private ChargingProfile chargingProfile;

}
