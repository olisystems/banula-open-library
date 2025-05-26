package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.AuthMethod;
import com.banula.openlib.ocpi.model.enums.SessionStatus;
import com.banula.openlib.ocpi.model.vo.CdrToken;
import com.banula.openlib.ocpi.model.vo.ChargingPeriod;
import com.banula.openlib.ocpi.model.vo.Price;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChargingSession {

    private String id;
    private String countryCode;
    private String partyId;
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;
    private Float kwh;
    private CdrToken cdrToken;
    private AuthMethod authMethod;
    private String authorizationReference;
    private String locationId;
    private String evseUid;
    private String connectorId;
    private String meterId;
    private String currency;
    private List<ChargingPeriod> chargingPeriods;
    private Price totalCost;
    private SessionStatus status;
    private String startSignedData;
    private String endSignedData;

    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
