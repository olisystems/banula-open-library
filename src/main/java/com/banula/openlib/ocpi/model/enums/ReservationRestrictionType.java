package com.banula.openlib.ocpi.model.enums;

public enum ReservationRestrictionType {
    /**
     * Used in TariffElements to describe costs for a reservation.
     */
    RESERVATION,
    /**
     * Used in TariffElements to describe costs for a reservation that expires (i.e.
     * driver does not start a
     * charging session before expiry_date of the reservation)
     */
    RESERVATION_EXPIRES
}
