package com.banula.openlib.ocpi.model.enums;

public enum ConnectionStatus {
    /**
     * Party is connected.
     */
    CONNECTED,
    /**
     * Party is currently not connected.
     */
    OFFLINE,
    /**
     * Connection to this party is planned, but has never been connected.
     */
    PLANNED,
    /**
     * Party is now longer active, will never connect anymore.
     */
    SUSPENDED
}
