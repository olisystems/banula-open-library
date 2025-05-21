package com.banula.ocpi.model.enums;

/**
 * The command requested.
 */
public enum CommandType {
    /**
     * Request the Charge Point to cancel a specific reservation.
     */
    CANCEL_RESERVATION,
    /**
     * Request the Charge Point to reserve a (specific) EVSE for a Token for a certain time, starting now.
     */
    RESERVE_NOW,
    /**
     * Request the Charge Point to start a transaction on the given EVSE/Connector.
     */
    START_SESSION,
    /**
     * Request the Charge Point to stop an ongoing session.
     */
    STOP_SESSION,
    /**
     * Request the Charge Point to unlock the connector (if applicable).
     * This functionality is for help desk operators only!
     */
    UNLOCK_CONNECTOR
}
