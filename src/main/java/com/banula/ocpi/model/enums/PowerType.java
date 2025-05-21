package com.banula.ocpi.model.enums;

public enum PowerType {
    /**
     * AC single phase
     */
    AC_1_PHASE,
    /**
     * AC two phases, only two of the three available phases connected.
     */
    AC_2_PHASE,
    /**
     * AC two phases using split phase system.
     */
    AC_2_PHASE_SPLIT,
    /**
     * AC two phases using split phase system.
     */
    AC_3_PHASE,
    /**
     * Direct Current.
     */
    DC
}
