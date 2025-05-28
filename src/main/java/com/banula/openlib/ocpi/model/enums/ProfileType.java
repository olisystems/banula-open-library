package com.banula.openlib.ocpi.model.enums;

/**
 * Different smart charging profile types.
 */
public enum ProfileType {
    /**
     * Driver wants to use the cheapest charging profile possible.
     */
    CHEAP,
    /**
     * Driver wants his EV charged as quickly as possible and is willing to pay a
     * premium for this, if needed.
     */
    FAST,
    /**
     * Driver wants his EV charged with as much regenerative (green) energy as
     * possible.
     */
    GREEN,
    /**
     * Driver does not have special preferences.
     */
    REGULAR
}
