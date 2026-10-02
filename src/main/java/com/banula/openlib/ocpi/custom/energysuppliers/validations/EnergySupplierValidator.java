package com.banula.openlib.ocpi.custom.energysuppliers.validations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.banula.openlib.ocpi.custom.energysuppliers.EnergySupplier;
import com.banula.openlib.ocpi.custom.energysuppliers.EnergySupplierTsoEntry;
import com.banula.openlib.ocpi.custom.energysuppliers.MaloOwnerType;

/**
 * The rules an energy supplier has to meet before it is stored. Errors are keyed by JSON field, e.g.
 * {@code per_tso[1].malo_id}, so a form can show each one next to its input.
 */
public class EnergySupplierValidator {

    /** 13-digit BDEW code or 16-character EIC code. */
    public static final Pattern MARKET_PARTNER_ID = Pattern.compile("^(\\d{13}|[A-Z0-9-]{16})$");
    /** 2-digit issuing office, object type letter, 12 code characters and a check character. */
    public static final Pattern EIC = Pattern.compile("^\\d{2}[A-Z][A-Z0-9-]{12}[A-Z0-9]$");
    public static final Pattern MALO_ID = Pattern.compile("^\\d{11}$");
    public static final Pattern MABIS_METERING_POINT = Pattern.compile("^[A-Z0-9]{33}$");
    public static final int MAX_TEXT_LENGTH = 64;
    private static final String EIC_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-";

    private EnergySupplierValidator() {
    }

    /**
     * Trims every field, upper-cases the IDs and tags every MaLo as owned by the ES. Returns a new
     * object; the input is not changed.
     */
    public static EnergySupplier normalize(EnergySupplier request) {
        if (request == null) {
            return null;
        }
        List<EnergySupplierTsoEntry> perTso = request.getPerTso() == null ? List.of()
                : request.getPerTso().stream()
                        .map(entry -> EnergySupplierTsoEntry.builder()
                                .tsoMarketPartnerId(entry == null ? null : upper(entry.getTsoMarketPartnerId()))
                                .maloId(entry == null ? null : upper(entry.getMaloId()))
                                .mabisMeteringPoint(entry == null ? null : upper(entry.getMabisMeteringPoint()))
                                .ownerType(MaloOwnerType.ES)
                                .build())
                        .toList();
        return request.toBuilder()
                .esMarketPartnerId(upper(request.getEsMarketPartnerId()))
                .name(trim(request.getName()))
                .bkvMarketPartnerId(upper(request.getBkvMarketPartnerId()))
                .balancingGroupId(upper(request.getBalancingGroupId()))
                .bkzeReference(trim(request.getBkzeReference()))
                .perTso(new ArrayList<>(perTso))
                .build();
    }

    /**
     * @param keyId          the ES market partner ID the supplier is stored under; a different ID in the
     *                       body is rejected, so the key can never change
     * @param energySupplier the supplier, already {@link #normalize normalized}
     * @param knownTsoIds    the TSO market partner IDs of the configured TSO control areas
     * @return the field errors; empty when the supplier is valid
     */
    public static Map<String, String> validate(String keyId, EnergySupplier energySupplier, Set<String> knownTsoIds) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (energySupplier == null) {
            errors.put("body", "Request body is required");
            return errors;
        }

        if (energySupplier.getEsMarketPartnerId() != null && !energySupplier.getEsMarketPartnerId().equals(keyId)) {
            errors.put("es_market_partner_id", "The ES market partner ID cannot change (expected " + keyId + ")");
        }
        if (keyId == null || !MARKET_PARTNER_ID.matcher(keyId).matches()) {
            errors.put("es_market_partner_id", "ES market partner ID must be 13 digits (BDEW) or 16 characters (EIC)");
        }
        if (isBlank(energySupplier.getName())) {
            errors.put("name", "Name is required");
        } else if (energySupplier.getName().length() > MAX_TEXT_LENGTH) {
            errors.put("name", "Name must be at most " + MAX_TEXT_LENGTH + " characters");
        }
        if (isBlank(energySupplier.getBkvMarketPartnerId())
                || !MARKET_PARTNER_ID.matcher(energySupplier.getBkvMarketPartnerId()).matches()) {
            errors.put("bkv_market_partner_id", "BKV market partner ID must be 13 digits (BDEW) or 16 characters (EIC)");
        }
        if (!isValidEic(energySupplier.getBalancingGroupId())) {
            errors.put("balancing_group_id", "Balancing group ID must be a valid 16-character EIC code");
        }
        if (energySupplier.getBkzeReference() != null && energySupplier.getBkzeReference().length() > MAX_TEXT_LENGTH) {
            errors.put("bkze_reference", "BKZE reference must be at most " + MAX_TEXT_LENGTH + " characters");
        }

        validatePerTso(energySupplier.getPerTso() == null ? List.of() : energySupplier.getPerTso(),
                knownTsoIds == null ? Set.of() : knownTsoIds, errors);
        return errors;
    }

    private static void validatePerTso(List<EnergySupplierTsoEntry> perTso, Set<String> knownTsoIds,
            Map<String, String> errors) {
        if (perTso.size() > EnergySupplier.MAX_TSO_ENTRIES) {
            errors.put("per_tso", "At most " + EnergySupplier.MAX_TSO_ENTRIES + " TSO control areas are allowed");
            return;
        }
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < perTso.size(); i++) {
            EnergySupplierTsoEntry entry = perTso.get(i) == null ? new EnergySupplierTsoEntry() : perTso.get(i);
            String prefix = "per_tso[" + i + "].";
            String tso = entry.getTsoMarketPartnerId();
            if (isBlank(tso) || !knownTsoIds.contains(tso)) {
                errors.put(prefix + "tso_market_partner_id", "Choose one of the configured TSO control areas");
            } else if (!seen.add(tso)) {
                errors.put(prefix + "tso_market_partner_id", "Each TSO control area can only be used once");
            }
            if (isBlank(entry.getMaloId()) || !MALO_ID.matcher(entry.getMaloId()).matches()) {
                errors.put(prefix + "malo_id", "MaLo ID must be exactly 11 digits");
            }
            if (isBlank(entry.getMabisMeteringPoint())
                    || !MABIS_METERING_POINT.matcher(entry.getMabisMeteringPoint()).matches()) {
                errors.put(prefix + "mabis_metering_point",
                        "Mabis metering point must be exactly 33 characters (digits or capital letters)");
            }
        }
    }

    /**
     * Checks the EIC structure and its check character (ENTSO-E): the first 15 characters, weighted 16 down
     * to 2, are summed and the check character is {@code 36 - ((sum - 1) mod 37)}; '-' is never valid.
     */
    public static boolean isValidEic(String value) {
        if (value == null || !EIC.matcher(value).matches()) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            sum += EIC_ALPHABET.indexOf(value.charAt(i)) * (16 - i);
        }
        return EIC_ALPHABET.charAt(36 - ((sum - 1) % 37)) == value.charAt(15);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static String upper(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
