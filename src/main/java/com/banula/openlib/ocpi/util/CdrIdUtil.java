package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.model.CDR;
import com.banula.openlib.ocpi.model.dto.CdrDTO;
import java.util.Optional;

public class CdrIdUtil {

    private CdrIdUtil() {
    }

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

    public static String generateCdrIdForLocationFieldInResponseHeader(String serviceUrl, CdrDTO cdr) {
        return serviceUrl + "/api/v1/internal/ocpi/2.2.1/cdrs/"
                + cdr.getCountryCode() + "*" + cdr.getPartyId() + "*"
                + cdr.getId();
    }

}
