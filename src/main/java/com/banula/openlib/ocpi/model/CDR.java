package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.AuthMethod;
import com.banula.openlib.ocpi.model.vo.*;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CDR {

    /**
     * ISO-3166 alpha-2 country code of the CPO that 'owns' this CDR.
     */
    @JsonProperty("country_code")
    @NotEmpty(message = "Country code cannot be empty")
    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
    private String countryCode;

    /**
     * ID of the CPO that 'owns' this CDR (following the ISO-15118 standard).
     */
    @JsonProperty("party_id")
    @NotEmpty(message = "Party ID cannot be empty")
    @Size(min = 3, max = 3, message = "Party ID must be 3 characters")
    private String partyId;

    /**
     * Uniquely identifies the CDR, the ID SHALL be unique per country_code/party_id
     * combination.
     * This field is longer than the usual 36 characters to allow for credit CDRs to
     * have something appended to
     * the original ID. Normal (non-credit) CDRs SHALL only have an ID with a
     * maximum length of 36.
     */
    @JsonProperty("id")
    @NotEmpty(message = "CDR ID cannot be empty")
    @Size(min = 1, max = 255, message = "CDR ID must be between 1 and 255 characters")
    private String id;

    /**
     * Start timestamp of the charging session, or in-case of a reservation (before
     * the start of a session) the start
     * of the reservation.
     */
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Start date time cannot be null")
    private LocalDateTime startDateTime;

    /**
     * The timestamp when the session was completed/finished, charging might have
     * finished before the session ends,
     * for example: EV is full, but parking cost also has to be paid
     */
    @JsonProperty("end_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "End date time cannot be null")
    private LocalDateTime endDateTime;

    /**
     * Unique ID of the Session for which this CDR is sent. Is only allowed to be
     * omitted when
     * the CPO has not implemented the Sessions module or this CDR is the result of
     * a reservation that never became
     * a charging session, thus no OCPI Session.
     */
    @JsonProperty("session_id")
    @Size(max = 36, message = "Session ID cannot be longer than 36 characters")
    private String sessionId;

    /**
     * Token used to start this charging session, including all the relevant
     * information to identify the unique token.
     */
    @JsonProperty("cdr_token")
    @NotNull(message = "CDR token cannot be null")
    private CdrToken cdrToken;

    /**
     * Method used for authentication. Multiple
     * <mod_cdrs_authmethod_enum,AuthMethods>> are possible during
     * a charging sessions, for example when the session was started with a
     * reservation: ReserveNow: COMMAND.
     * When the session was started with a reservation: ReserveNow: COMMAND.
     */
    @JsonProperty("auth_method")
    @NotNull(message = "Authentication method cannot be null")
    private AuthMethod authMethod;

    /**
     * Reference to the authorization given by the eMSP. When the eMSP provided an
     * authorization_reference
     * in either: real-time authorization, StartSession or ReserveNow, this field
     * SHALL contain the same value.
     * When different authorization_reference values have been given by the eMSP
     * that are relevant to this Session,
     * the last given value SHALL be used here.
     */
    @JsonProperty("authorization_reference")
    @Size(max = 36, message = "Authorization reference cannot be longer than 36 characters")
    private String authorizationReference;

    /**
     * Location where the charging session took place, including only the relevant
     * EVSE and Connector.
     */
    @JsonProperty("cdr_location")
    @NotNull(message = "CDR location cannot be null")
    private CdrLocation cdrLocation;

    /**
     * Identification of the Meter inside the Charge Point.
     */
    @JsonProperty("meter_id")
    @Size(max = 255, message = "Meter ID cannot be longer than 255 characters")
    private String meterId;

    /**
     * Currency of the CDR in ISO 4217 Code.
     */
    @JsonProperty("currency")
    @NotEmpty(message = "Currency cannot be empty")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currency;

    /**
     * List of relevant Tariff Elements, see: Tariff. When relevant, a Free of
     * Charge tariff should also be in this list,
     * and point to a defined Free of Charge Tariff.
     */
    @JsonProperty("tariffs")
    private List<Tariff> tariffs;

    /**
     * List of Charging Periods that make up this charging session. A session
     * consists of 1 or more periods,
     * where each period has a different relevant Tariff.
     */
    @JsonProperty("charging_periods")
    private List<ChargingPeriod> chargingPeriods;

    /**
     * Signed data that belongs to this charging Session.
     */
    @JsonProperty("signed_data")
    private SignedData signedData;

    /**
     * Total sum of all the costs of this transaction in the specified currency.
     */
    @JsonProperty("total_cost")
    @NotNull(message = "Total cost cannot be null")
    private Price totalCost;

    /**
     * Total sum of all the fixed costs in the specified currency, except fixed
     * price components of parking
     * and reservation. The cost not depending on amount of time/energy used etc.
     * Can contain costs like a start tariff.
     */
    @JsonProperty("total_fixed_cost")
    private Price totalFixedCost;

    /**
     * Total energy charged, in kWh.
     */
    @JsonProperty("total_energy")
    @NotNull(message = "Total energy cannot be null")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Invalid total energy value")
    private Float totalEnergy;

    /**
     * Total sum of all the cost of all the energy used, in the specified currency.
     */
    @JsonProperty("total_energy_cost")
    private Price totalEnergyCost;

    /**
     * Total duration of the charging session (including the duration of charging
     * and not charging), in hours.
     */
    @JsonProperty("total_time")
    @NotNull(message = "Total time cannot be null")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Invalid total time value")
    private Float totalTime;

    /**
     * Total sum of all the cost related to duration of charging during this
     * transaction, in the specified currency.
     */
    @JsonProperty("total_time_cost")
    private Price totalTimeCost;

    /**
     * Total duration of the charging session where the EV was not charging
     * (no energy was transferred between EVSE and EV), in hours.
     */
    @JsonProperty("total_parking_time")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Invalid total parking time value")
    private Float totalParkingTime;

    /**
     * Total sum of all the cost related to parking of this transaction, including
     * fixed price components,
     * in the specified currency.
     */
    @JsonProperty("total_parking_cost")
    private Price totalParkingCost;

    /**
     * Total sum of all the cost related to a reservation of a Charge Point,
     * including fixed price components,
     * in the specified currency.
     */
    @JsonProperty("total_reservation_cost")
    private Price totalReservationCost;

    /**
     * Optional remark, can be used to provide additional human readable information
     * to the CDR,
     * for example: reason why a transaction was stopped.
     */
    @JsonProperty("remark")
    @Size(max = 255, message = "Remark cannot be longer than 255 characters")
    private String remark;

    /**
     * This field can be used to reference an invoice, that will later be send for
     * this CDR.
     * Making it easier to link a CDR to a given invoice. Maybe even group CDRs that
     * will be on the same invoice.
     */
    @JsonProperty("invoice_reference_id")
    @Size(max = 39, message = "Invoice reference ID cannot be longer than 39 characters")
    private String invoiceReferenceId;

    /**
     * When set to true, this is a Credit CDR, and the field credit_reference_id
     * needs to be set as well.
     */
    @JsonProperty("credit")
    private Boolean credit;

    /**
     * Is required to be set for a Credit CDR. This SHALL contain the id of the CDR
     * for which this is a Credit CDR.
     */
    @JsonProperty("credit_reference_id")
    @Size(max = 39, message = "Credit reference ID cannot be longer than 39 characters")
    private String creditReferenceId;

    /**
     * When set to true, this CDR is for a charging session using the home charger
     * of the EV Driver for which the
     * energy cost needs to be financial compensated to the EV Driver.
     */
    @JsonProperty("home_charging_compensation")
    private Boolean homeChargingCompensation;

    /**
     * Timestamp when this CDR was last updated (or created).
     */
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    @NotNull(message = "Last updated time cannot be null")
    private LocalDateTime lastUpdated;

}
