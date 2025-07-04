package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.AuthMethod;
import com.banula.openlib.ocpi.model.enums.SessionStatus;
import com.banula.openlib.ocpi.model.vo.CdrToken;
import com.banula.openlib.ocpi.model.vo.ChargingPeriod;
import com.banula.openlib.ocpi.model.vo.Price;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class ChargingSessionDTO {
    @NotEmpty
    @Size(min = 1, max = 2)
    @JsonProperty("country_code")
    private String countryCode;
    @NotEmpty
    @Size(min = 1, max = 3)
    @JsonProperty("party_id")
    private String partyId;
    @NotEmpty
    @Size(min = 1, max = 36)
    @JsonProperty("id")
    private String id;
    @NotNull
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;
    @JsonProperty("end_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;
    @NotNull
    @Digits(integer = Integer.MAX_VALUE, fraction = 4)
    @JsonProperty("kwh")
    private Float kwh;
    @NotNull
    @JsonProperty("cdr_token")
    @Valid
    private CdrToken cdrToken;
    @NotNull
    @JsonProperty("auth_method")
    private AuthMethod authMethod;
    @Size(min = 1, max = 36)
    @NotEmpty(message = "authorization_reference is required in Banula style of charging")
    @JsonProperty("authorization_reference")
    private String authorizationReference;
    @NotEmpty
    @Size(min = 1, max = 36)
    @JsonProperty("location_id")
    private String locationId;
    @NotEmpty
    @Size(min = 1, max = 36)
    @JsonProperty("evse_uid")
    private String evseUid;
    @NotBlank
    @Size(min = 1, max = 36)
    @JsonProperty("connector_id")
    private String connectorId;
    @Size(min = 1, max = 255)
    @JsonProperty("meter_id")
    private String meterId;
    @NotBlank
    @Size(min = 1, max = 3)
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("charging_periods")
    private List<ChargingPeriod> chargingPeriods;
    @JsonProperty("total_cost")
    @NotNull(message = "total_cost is required in Banula style of charging")
    private Price totalCost;
    @NotNull
    @JsonProperty("status")
    private SessionStatus status;

    @NotNull
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
