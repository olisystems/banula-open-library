package com.banula.ocpi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.model.enums.AuthMethod;
import com.banula.ocpi.model.enums.SessionStatus;
import com.banula.ocpi.model.vo.CdrToken;
import com.banula.ocpi.model.vo.ChargingPeriod;
import com.banula.ocpi.model.vo.Price;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@Builder
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
