package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.ChargingSession;
import com.banula.openlib.ocpi.model.dto.ChargingSessionDTO;

public class ChargingSessionMapper {

    public static ChargingSession toChargingSessionEntity(ChargingSessionDTO sessionDTO) {
        if (sessionDTO == null)
            return null;
        return ChargingSession.builder()
                .chargingPeriods(sessionDTO.getChargingPeriods())
                .id(sessionDTO.getId())
                .authMethod(sessionDTO.getAuthMethod())
                .currency(sessionDTO.getCurrency())
                .kwh(sessionDTO.getKwh())
                .cdrToken(sessionDTO.getCdrToken())
                .connectorId(sessionDTO.getConnectorId())
                .evseUid(sessionDTO.getEvseUid())
                .authorizationReference(sessionDTO.getAuthorizationReference())
                .countryCode(sessionDTO.getCountryCode())
                .endDateTime(sessionDTO.getEndDateTime())
                .startDateTime(sessionDTO.getStartDateTime())
                .lastUpdated(sessionDTO.getLastUpdated())
                .meterId(sessionDTO.getMeterId())
                .locationId(sessionDTO.getLocationId())
                .partyId(sessionDTO.getPartyId())
                .status(sessionDTO.getStatus())
                .totalCost(sessionDTO.getTotalCost())
                .build();
    }

    public static ChargingSessionDTO toChargingSessionDTO(ChargingSession chargingSession) {
        if (chargingSession == null)
            return null;
        return ChargingSessionDTO.builder()
                .chargingPeriods(chargingSession.getChargingPeriods())
                .id(chargingSession.getId())
                .authMethod(chargingSession.getAuthMethod())
                .currency(chargingSession.getCurrency())
                .kwh(chargingSession.getKwh())
                .cdrToken(chargingSession.getCdrToken())
                .connectorId(chargingSession.getConnectorId())
                .evseUid(chargingSession.getEvseUid())
                .authorizationReference(chargingSession.getAuthorizationReference())
                .countryCode(chargingSession.getCountryCode())
                .endDateTime(chargingSession.getEndDateTime())
                .startDateTime(chargingSession.getStartDateTime())
                .lastUpdated(chargingSession.getLastUpdated())
                .meterId(chargingSession.getMeterId())
                .locationId(chargingSession.getLocationId())
                .partyId(chargingSession.getPartyId())
                .status(chargingSession.getStatus())
                .totalCost(chargingSession.getTotalCost())
                .authMethod(chargingSession.getAuthMethod())
                .build();
    }
}
