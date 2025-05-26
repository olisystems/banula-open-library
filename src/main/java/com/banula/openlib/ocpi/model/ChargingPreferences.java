package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Contains the charging preferences of an EV driver.
 */
@Data
@ToString
@NoArgsConstructor
public class ChargingPreferences {

    /**
     * Type of Smart Charging Profile selected by the driver. The ProfileType has to
     * be supported at the Connector
     * and for every supported ProfileType, a Tariff MUST be provided. This gives
     * the EV driver the option between
     * different pricing options.
     */
    @NotNull(message = "Profile type must not be null")
    @JsonProperty("profile_type")
    private ProfileType profileType;

    /**
     * Expected departure. The driver has given this Date/Time as expected departure
     * moment. It is only an estimation
     * and not necessarily the Date/Time of the actual departure.
     */
    @JsonProperty("departure_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime departureTime;

    /**
     * Requested amount of energy in kWh. The EV driver wants to have this amount
     * of energy charged.
     */
    @JsonProperty("energyNeed")
    private Float energyNeed;

    /**
     * The driver allows their EV to be discharged when needed, as long as the other
     * preferences are met: EV is charged
     * with the preferred energy (energy_need) until the preferred departure moment
     * (departure_time).
     * Default if omitted: false
     */
    @JsonProperty("discharge_allowed")
    private Boolean dischargeAllowed = false;

}
