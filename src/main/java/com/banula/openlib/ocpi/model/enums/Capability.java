package com.banula.openlib.ocpi.model.enums;

public enum Capability {
    /**
     * The EVSE supports charging profiles.
     */
    CHARGING_PROFILE_CAPABLE,
    /**
     * The EVSE supports charging preferences.
     */
    CHARGING_PREFERENCES_CAPABLE,
    /**
     * EVSE has a payment terminal that supports chip cards.
     */
    CHIP_CARD_SUPPORT,
    /**
     * EVSE has a payment terminal that supports contactless cards.
     */
    CONTACTLESS_CARD_SUPPORT,
    /**
     * EVSE has a payment terminal that makes it possible to pay for charging using
     * a credit card.
     */
    CREDIT_CARD_PAYABLE,
    /**
     * EVSE has a payment terminal that makes it possible to pay for charging using
     * a debit card.
     */
    DEBIT_CARD_PAYABLE,
    /**
     * EVSE has a payment terminal with a pin-code entry device.
     */
    PED_TERMINAL,
    /**
     * EVSE has a payment terminal with a pin-code entry device.
     */
    REMOTE_START_STOP_CAPABLE,
    /**
     * EVSE has a payment terminal with a pin-code entry device.
     */
    RESERVABLE,
    /**
     * Charging at this EVSE can be authorized with an RFID token.
     */
    RFID_READER,
    /**
     * When a StartSession is sent to this EVSE, the MSP is required to add the
     * optional connector_id field
     * in the StartSession object.
     */
    START_SESSION_CONNECTOR_REQUIRED,
    /**
     * When a StartSession is sent to this EVSE, the MSP is required to add the
     * optional connector_id field in the
     * StartSession object.
     */
    TOKEN_GROUP_CAPABLE,
    /**
     * Connectors have mechanical lock that can be requested by the eMSP to be
     * unlocked.
     */
    UNLOCK_CAPABLE
}
