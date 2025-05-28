package com.banula.openlib.ocpi.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines when authorization of a Token by the CPO is allowed.
 */
public enum WhitelistType {
    /**
     * Token always has to be whitelisted, realtime authorization is not
     * possible/allowed. CPO shall always allow
     * any use of this Token.
     */
    @JsonProperty("ALWAYS")
    ALWAYS,
    /**
     * It is allowed to whitelist the token, realtime authorization is also allowed.
     * The CPO may choose which version
     * of authorization to use.
     */
    @JsonProperty("ALLOWED")
    ALLOWED,
    /**
     * In normal situations realtime authorization shall be used. But when the CPO
     * cannot get a response from the eMSP
     * (communication between CPO and eMSP is offline), the CPO shall allow this
     * Token to be used.
     */
    @JsonProperty("ALLOWED_OFFLINE")
    ALLOWED_OFFLINE,
    /**
     * Whitelisting is forbidden, only realtime authorization is allowed. CPO shall
     * always send a realtime
     * authorization for any use of this Token to the eMSP.
     */
    @JsonProperty("NEVER")
    NEVER
}
