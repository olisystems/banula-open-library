package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPreferencesDTO {
    @NotNull(message = "Profile type must not be blank")
    @JsonProperty("profile_type")
    private ProfileType profileType;

    @NotNull(message = "Departure time must not be null")
    @JsonProperty("departure_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime departureTime;

    @NotNull(message = "Energy need must not be null")
    @JsonProperty("energyNeed")
    private Float energyNeed;

    @NotNull(message = "Discharge allowed must not be null")
    @JsonProperty("discharge_allowed")
    private Boolean dischargeAllowed = false;
}
