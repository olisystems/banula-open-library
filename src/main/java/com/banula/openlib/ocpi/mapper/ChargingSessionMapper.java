package com.banula.openlib.ocpi.mapper;

import org.springframework.beans.BeanUtils;

import com.banula.openlib.ocpi.model.ChargingSession;
import com.banula.openlib.ocpi.model.dto.ChargingSessionDTO;

public class ChargingSessionMapper {

    public static ChargingSession toChargingSessionEntity(ChargingSessionDTO sessionDTO) {
        if (sessionDTO == null)
            return null;
        ChargingSession chargingSession = new ChargingSession();
        BeanUtils.copyProperties(sessionDTO, chargingSession);
        return chargingSession;

    }

    public static ChargingSessionDTO toChargingSessionDTO(ChargingSession chargingSession) {
        if (chargingSession == null)
            return null;
        ChargingSessionDTO chargingSessionDTO = new ChargingSessionDTO();
        BeanUtils.copyProperties(chargingSession, chargingSessionDTO);
        return chargingSessionDTO;
    }
}
