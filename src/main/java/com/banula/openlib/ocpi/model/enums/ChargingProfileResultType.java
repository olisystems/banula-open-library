package com.banula.openlib.ocpi.model.enums;

/**
 * Result of a ChargingProfile request that the EVSE sends via the CPO to the
 * eMSP.
 */
public enum ChargingProfileResultType {
    /**
     * ChargingProfile request accepted by the EVSE.
     */
    ACCEPTED,
    /**
     * ChargingProfile request rejected by the EVSE.
     */
    REJECTED,
    /**
     * No Charging Profile(s) were found by the EVSE matching the request.
     */
    UNKNOWN
}
