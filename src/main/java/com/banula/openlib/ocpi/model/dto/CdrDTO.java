package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.Tariff;
import com.banula.openlib.ocpi.model.enums.AuthMethod;
import com.banula.openlib.ocpi.model.vo.*;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CdrDTO {
    @NotEmpty(message = "Country code must not be blank")
    @Size(min = 1, max = 2, message = "Country code must be between 1 and 2 characters long")
    @JsonProperty("country_code")
    private String countryCode;

    @NotEmpty(message = "Party ID must not be blank")
    @Size(min = 1, max = 3, message = "Party ID must be between 1 and 3 characters long")
    @JsonProperty("party_id")
    private String partyId;

    @NotEmpty(message = "ID must not be blank")
    @Size(min = 1, max = 39, message = "ID must be between 1 and 39 characters long")
    private String id;

    @NotNull(message = "Start date time must not be blank")
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @NotNull(message = "End date time must not be blank")
    @JsonProperty("end_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    @Size(min = 1, max = 36, message = "Session ID must be between 1 and 36 characters long")
    @JsonProperty("session_id")
    private String sessionId;

    @NotNull(message = "CDR token must not be blank")
    @JsonProperty("cdr_token")
    @Valid
    private CdrToken cdrToken;

    @NotNull(message = "Auth method must not be blank")
    @JsonProperty("auth_method")
    private AuthMethod authMethod;

    @Size(min = 1, max = 36, message = "Authorization reference must be between 1 and 36 characters long")
    @JsonProperty("authorization_reference")
    private String authorizationReference;

    @NotNull(message = "CDR location must not be blank")
    @JsonProperty("cdr_location")
    @Valid
    private CdrLocation cdrLocation;

    @JsonProperty("meter_id")
    private String meterId;

    private String currency;

    private List<Tariff> tariffs;

    @NotNull(message = "Charging periods must not be blank")
    @JsonProperty("charging_periods")
    private List<ChargingPeriod> chargingPeriods;

    @JsonProperty("signed_data")
    @Valid
    private SignedData signedData;

    @NotNull(message = "Total cost must not be blank")
    @JsonProperty("total_cost")
    @Valid
    private Price totalCost;

    @JsonProperty("total_fixed_cost")
    @Valid
    private Price totalFixedCost;

    @NotNull(message = "Total energy must not be blank")
    @JsonProperty("total_energy")
    private Float totalEnergy;

    @JsonProperty("total_energy_cost")
    @Valid
    private Price totalEnergyCost;

    @NotNull(message = "Total time must not be blank")
    @JsonProperty("total_time")
    private Float totalTime;

    @JsonProperty("total_time_cost")
    @Valid
    private Price totalTimeCost;

    @JsonProperty("total_parking_time")
    private Float totalParkingTime;

    @JsonProperty("total_parking_cost")
    private Price totalParkingCost;

    @JsonProperty("total_reservation_cost")
    @Valid
    private Price totalReservationCost;

    @Size(min = 1, max = 255, message = "Remark must be between 1 and 255 characters long")
    private String remark;

    @Size(min = 1, max = 39, message = "Invoice reference ID must be between 1 and 39 characters long")
    @JsonProperty("invoice_reference_id")
    private String invoiceReferenceId;

    private Boolean credit;

    @Size(min = 1, max = 39, message = "Credit reference ID must be between 1 and 39 characters long")
    @JsonProperty("credit_reference_id")
    private String creditReferenceId;

    @JsonProperty("home_charging_compensation")
    private Boolean homeChargingCompensation;

    @NotNull(message = "Last updated must not be blank")
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
