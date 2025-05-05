package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.model.enums.AuthMethod;
import com.banula.ocpi.model.enums.SessionStatus;
import com.banula.ocpi.model.vo.CdrToken;
import com.banula.ocpi.model.vo.ChargingPeriod;
import com.banula.ocpi.model.vo.Price;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Session {

    /**
     * ISO-3166 alpha-2 country code of the CPO that 'owns' this Session.
     */
    @JsonProperty("country_code")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    private String countryCode;

    /**
     * ID of the CPO that 'owns' this Session (following the ISO-15118 standard).
     */
    @JsonProperty("party_id")
    @Size(min = 3, max = 3, message = "Party ID must be 3 characters")
    private String partyId;

    /**
     * ID of the CPO that 'owns' this Session (following the ISO-15118 standard).
     */
    @JsonProperty("id")
    @Size(max = 36, message = "ID must be maximum 36 characters")
    private String id;

    /**
     * The timestamp when the session became ACTIVE in the Charge Point. When the session is still PENDING,
     * this field SHALL be set to the time the Session was created at the Charge Point. When a Session goes from PENDING
     * to ACTIVE, this field SHALL be updated to the moment the Session went to ACTIVE in the Charge Point.
     */
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Start date time is required")
    private LocalDateTime startDateTime;

    /**
     * The timestamp when the session was completed/finished, charging might have finished before the session ends,
     * for example: EV is full, but parking cost also has to be paid.
     */
    @JsonProperty("end_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    /**
     * How many kWh were charged.
     */
    @JsonProperty("kwh")
    @NotNull(message = "kwh is required")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Invalid kwh format")
    private Float kwh;

    /**
     * Token used to start this charging session, including all the relevant information to identify the unique token.
     */
    @JsonProperty("cdr_token")
    @NotNull(message = "cdr_token is required")
    @Valid
    private CdrToken cdrToken;

    /**
     * Method used for authentication. This might change during a session, for example when the session was started
     * with a reservation: ReserveNow: COMMAND. When the driver arrives and starts charging using a Token that
     * is whitelisted: WHITELIST.
     */
    @JsonProperty("auth_method")
    @NotNull(message = "auth_method is required")
    private AuthMethod authMethod;

    /**
     * Reference to the authorization given by the eMSP. When the eMSP provided an authorization_reference
     * in either: real-time authorization, StartSession or ReserveNow this field SHALL contain the same value.
     * When different authorization_reference values have been given by the eMSP that are relevant to this Session,
     * the last given value SHALL be used here.
     */
    @JsonProperty("authorization_reference")
    @Size(max = 36, message = "Authorization reference must be maximum 36 characters")
    private String authorizationReference;

    /**
     * Location.id of the Location object of this CPO, on which the charging session is/was happening.
     */
    @JsonProperty("location_id")
    @Size(max = 36, message = "Location ID must be maximum 36 characters")
    private String locationId;

    /**
     * EVSE.uid of the EVSE of this Location on which the charging session is/was happening.
     * Allowed to be set to: #NA when this session is created for a reservation, but no EVSE yet assigned to the driver.
     */
    @JsonProperty("evse_uid")
    @Size(max = 36, message = "EVSE UID must be maximum 36 characters")
    private String evseUid;

    /**
     * Connector.id of the Connector of this Location where the charging session is/was happening.
     * Allowed to be set to: #NA when this session is created for a reservation, but no connector yet assigned
     * to the driver.
     */
    @JsonProperty("connector_id")
    @Size(max = 36, message = "Connector ID must be maximum 36 characters")
    private String connectorId;

    /**
     * Optional identification of the kWh meter.
     */
    @JsonProperty("meter_id")
    @Size(max = 255, message = "Meter ID must be maximum 255 characters")
    private String meterId;

    /**
     * ISO 4217 code of the currency used for this session.
     */
    @JsonProperty("currency")
    @Size(max = 3, message = "Currency code must be 3 characters")
    private String currency;

    /**
     * An optional list of Charging Periods that can be used to calculate and verify the total cost.
     */
    @JsonProperty("charging_periods")
    private List<ChargingPeriod> chargingPeriods;

    /**
     * The total cost of the session in the specified currency. This is the price that the eMSP will have to pay
     * to the CPO.
     */
    @JsonProperty("total_cost")
    private Price totalCost;

    /**
     * The status of the session.
     */
    @JsonProperty("status")
    @NotNull(message = "Session status is required")
    private SessionStatus status;

    /**
     * Timestamp when this Session was last updated (or created).
     */
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Last updated time is required")
    private LocalDateTime lastUpdated;

}
