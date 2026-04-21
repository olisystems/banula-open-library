package com.banula.openlib.ocpi.util;

import java.util.Optional;

public class CdrIdUtil {

    private CdrIdUtil() {}

    /**
     * Splits a CDR id that uses the composite key format (countryCode*partyId*id)
     * into its three components.
     *
     * @param id the raw id string, potentially in the format "DE*ABC*57723921"
     * @return Optional containing [countryCode, partyId, cdrId] if the id matches
     *         the composite key format, or empty if it does not
     */
    public static Optional<String[]> splitCdrIdIntoCompoundIndex(String id) {
        if (id == null || !id.contains("*")) {
            return Optional.empty();
        }
        String[] parts = id.split("\\*");
        if (parts.length != 3) {
            return Optional.empty();
        }
        return Optional.of(parts);
    }
}
