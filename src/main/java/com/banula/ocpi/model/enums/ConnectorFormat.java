package com.banula.ocpi.model.enums;

public enum ConnectorFormat {
    /**
     * The connector is a socket; the EV user needs to bring a fitting plug.
     */
    SOCKET,
    /**
     * The connector is an attached cable; the EV users car needs to have a fitting inlet.
     */
    CABLE
}
