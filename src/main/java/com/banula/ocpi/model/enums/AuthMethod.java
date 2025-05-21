package com.banula.ocpi.model.enums;

public enum AuthMethod {
    /**
     * Authentication request has been sent to the eMSP.
     */
    AUTH_REQUEST,
    /**
     * Command like StartSession or ReserveNow used to start the Session, the Token provided in the Command was
     * used as authorization.
     */
    COMMAND,
    /**
     * Whitelist used for authentication, no request to the eMSP has been performed.
     */
    WHITELIST
}
