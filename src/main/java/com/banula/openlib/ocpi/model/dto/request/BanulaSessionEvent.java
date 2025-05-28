package com.banula.openlib.ocpi.model.dto.request;

import com.banula.openlib.ocpi.model.enums.ChargingEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BanulaSessionEvent {

    private String countryCode;
    private String partyId;
    private String id;
    private String startDateTime;
    private String endDateTime;
    private Float kwh;
    private Float startMeterValue;
    private Float stopMeterValue;
    private String cdrToken;
    private String authMethod;
    private String authorizationReference;
    private String locationId;
    private String evseUid;
    private String connectorId;
    private String meterId;
    private String currency;
    private String totalCost;
    private String status;
    private String lastUpdated;
    private boolean isFirstChargingEvent;
    private String paymentIntentId;
    private ChargingEventType chargingEventType;

}
