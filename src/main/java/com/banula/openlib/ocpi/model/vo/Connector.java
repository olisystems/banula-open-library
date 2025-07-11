package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ConnectorFormat;
import com.banula.openlib.ocpi.model.enums.ConnectorType;
import com.banula.openlib.ocpi.model.enums.PowerType;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A Connector is the socket or cable and plug available for the EV to use. A
 * single EVSE may provide
 * multiple Connectors but only one of them can be in use at the same time.
 * A Connector always belongs to an EVSE object.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Connector {

    /**
     * Identifier of the Connector within the EVSE. Two Connectors may have
     * the same id as long as they do not belong to the same EVSE object.
     */
    @JsonProperty("id")
    @NotNull(message = "id must not be null")
    @Size(max = 36, message = "id must be at most 36 characters")
    private String id;

    /**
     * The standard of the installed connector.
     */
    @JsonProperty("standard")
    @NotNull(message = "standard must not be null")
    private ConnectorType standard;

    /**
     * The format (socket/cable) of the installed connector.
     */
    @JsonProperty("format")
    @NotNull(message = "format must not be null")
    private ConnectorFormat format;

    @JsonProperty("power_type")
    @NotNull(message = "power_type must not be null")
    private PowerType powerType;

    /**
     * Maximum voltage of the connector (line to neutral for AC_3_PHASE), in volt
     * [V].
     * For example: DC Chargers might vary the voltage during charging when battery
     * almost full.
     */
    @JsonProperty("max_voltage")
    @NotNull(message = "max_voltage must not be null")
    private Integer maxVoltage;

    /**
     * Maximum amperage of the connector, in ampere [A].
     */
    @JsonProperty("max_amperage")
    @NotNull(message = "max_amperage must not be null")
    private Integer maxAmperage;

    /**
     * Maximum electric power that can be delivered by this connector, in Watts (W).
     * When the maximum electric power
     * is lower than the calculated value from voltage and amperage, this value
     * should be set.
     * For example: A DC Charge Point which can delivers up to 920V and up to 400A
     * can be limited to a maximum
     * of 150kW (max_electric_power = 150000). Depending on the car, it may supply
     * max voltage or current,
     * but not both at the same time. For AC Charge Points, the amount of phases
     * used can also have influence on the
     * maximum power
     */
    @NotNull(message = "max_electric_power is a required field in Banula style of Charging")
    @JsonProperty("max_electric_power")
    private Integer maxElectricPower;

    /**
     * Identifiers of the currently valid charging tariffs. Multiple tariffs are
     * possible, but only one of
     * each Tariff.type can be active at the same time. Tariffs with the same type
     * are only allowed if they
     * are not active at the same time: start_date_time and end_date_time period not
     * overlapping.
     * When preference-based smart charging is supported, one tariff for every
     * possible ProfileType should be provided.
     * These tell the user about the options they have at this Connector, and what
     * the tariff is for every option.
     * For a "free of charge" tariff, this field should be set and point to a
     * defined "free of charge" tariff.
     */
    @JsonProperty("tariff_ids")
    @Size(max = 36, message = "tariff_ids must be at most 36 characters")
    @NotNull(message = "tariff_ids is a required field in Banula style of Charging")
    private List<@NotEmpty(message = "tariff_id must not be empty") String> tariffIds;

    /**
     * URL to the operator’s terms and conditions.
     */
    @JsonProperty("terms_and_conditions")
    @Size(max = 255, message = "terms_and_conditions must be at most 255 characters")
    private String termsAndConditions;

    /**
     * Timestamp when this Connector was last updated (or created).
     */
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "last_updated must not be null")
    private LocalDateTime lastUpdated;

}
