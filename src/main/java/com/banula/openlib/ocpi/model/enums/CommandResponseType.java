package com.banula.openlib.ocpi.model.enums;

/**
 * Response to the command request from the eMSP to the CPO.
 */
public enum CommandResponseType {
    /**
     * The requested command is not supported by this CPO, Charge Point, EVSE etc.
     */
    NOT_SUPPORTED,
    /**
     * Command request rejected by the CPO. (Session might not be from a customer of
     * the eMSP that send this request)
     */
    REJECTED,
    /**
     * Command request accepted by the CPO.
     */
    ACCEPTED,
    /**
     * The Session in the requested command is not known by this CPO.
     */
    UNKNOWN_SESSION
}
