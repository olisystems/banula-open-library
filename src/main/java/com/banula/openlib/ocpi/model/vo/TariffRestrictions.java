package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.DayOfWeek;
import com.banula.openlib.ocpi.model.enums.ReservationRestrictionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TariffRestrictions {

    /**
     * Start time of day in local time, the time zone is defined in the time_zone
     * field of the Location,
     * for example 13:30, valid from this time of the day. Must be in 24h
     * format with leading zeros. Hour/Minute separator: ":" Regex:
     * ([0-1][0-9]|2[0-3]):[0-5][0-9]
     */
    @JsonProperty("start_time")
    @NotNull
    private String startTime;

    /**
     * End time of day in local time, the time zone is defined in the time_zone
     * field of the Location,
     * for example 19:45, valid until this time of the day. Same syntax as
     * start_time.
     * If end_time < start_time then the period wraps around to the next day. To
     * stop at end of the day use: 00:00.
     */
    @JsonProperty("end_time")
    @NotNull
    private String endTime;

    /**
     * Start date in local time, the time zone is defined in the time_zone field of
     * the Location,
     * for example: 2015-12-24, valid from this day (inclusive).
     * Regex: ([12][0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])
     */
    @JsonProperty("start_date")
    @NotNull
    private String startDate;

    /**
     * End date in local time, the time zone is defined in the time_zone field of
     * the Location,
     * for example: 2015-12-27, valid until this day (exclusive). Same syntax as
     * start_date.
     */
    @JsonProperty("end_date")
    @NotNull
    private String endDate;

    /**
     * Minimum consumed energy in kWh, for example 20, valid from this amount of
     * energy (inclusive) being used.
     */
    @JsonProperty("min_kwh")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float minKwh;

    /**
     * Maximum consumed energy in kWh, for example 50, valid until this amount of
     * energy (exclusive) being used.
     */
    @JsonProperty("max_kwh")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float maxKwh;

    /**
     * Sum of the minimum current (in Amperes) over all phases, for example 5. When
     * the EV is charging with more than,
     * or equal to, the defined amount of current, this TariffElement is/becomes
     * active.
     * If the charging current is or becomes lower, this TariffElement is not or no
     * longer valid and becomes inactive.
     * This describes NOT the minimum current over the entire Charging Session.
     * This restriction can make a TariffElement become active when the charging
     * current is above the defined value,
     * but the TariffElement MUST no longer be active when the charging current
     * drops below the defined value.
     */
    @JsonProperty("min_current")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float minCurrent;

    /**
     * Sum of the maximum current (in Amperes) over all phases, for example 20.
     * When the EV is charging with less than the defined amount of current, this
     * TariffElement becomes/is active.
     * If the charging current is or becomes higher, this TariffElement is not or no
     * longer valid and becomes inactive.
     * This describes NOT the maximum current over the entire Charging Session. This
     * restriction can make a TariffElement
     * become active when the charging current is below this value, but the
     * TariffElement MUST no longer be active when
     * the charging current raises above the defined value
     */
    @JsonProperty("max_current")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float maxCurrent;

    /**
     * Minimum power in kW, for example 5. When the EV is charging with more than,
     * or equal to, the defined amount of power,
     * this TariffElement is/becomes active. If the charging power is or becomes
     * lower,
     * this TariffElement is not or no longer valid and becomes inactive. This
     * describes NOT the minimum power over the
     * entire Charging Session. This restriction can make a TariffElement become
     * active when the charging power is
     * above this value, but the TariffElement MUST no longer be active when the
     * charging power drops below the defined value.
     */
    @JsonProperty("min_power")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float minPower;

    /**
     * Maximum power in kW, for example 20. When the EV is charging with less than
     * the defined amount of power,
     * this TariffElement becomes/is active. If the charging power is or becomes
     * higher, this TariffElement is not
     * or no longer valid and becomes inactive. This describes NOT the maximum power
     * over the entire Charging Session.
     * This restriction can make a TariffElement become active when the charging
     * power is below this value,
     * but the TariffElement MUST no longer be active when the charging power raises
     * above the defined value
     */
    @JsonProperty("max_power")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    private Float maxPower;

    /**
     * Minimum duration in seconds the Charging Session MUST last (inclusive). When
     * the duration of a Charging Session
     * is longer than the defined value, this TariffElement is or becomes active.
     * Before that moment,
     * this TariffElement is not yet active.
     */
    @JsonProperty("min_duration")
    private Integer minDuration;

    /**
     * Maximum duration in seconds the Charging Session MUST last (exclusive). When
     * the duration of a Charging Session
     * is shorter than the defined value, this TariffElement is or becomes active.
     * After that moment,
     * this TariffElement is no longer active.
     */
    @JsonProperty("max_duration")
    private Integer maxDuration;

    /**
     * Which day(s) of the week this TariffElement is active
     */
    @JsonProperty("day_of_week")
    private List<DayOfWeek> dayOfWeek;

    /**
     * When this field is present, the TariffElement describes reservation costs. A
     * reservation starts when
     * the reservation is made, and ends when the driver starts charging on the
     * reserved EVSE/Location, or when
     * the reservation expires. A reservation can only have: FLAT and TIME
     * TariffDimensions, where TIME is for the
     * duration of the reservation.
     */
    @JsonProperty("reservation")
    private ReservationRestrictionType reservation;
}
