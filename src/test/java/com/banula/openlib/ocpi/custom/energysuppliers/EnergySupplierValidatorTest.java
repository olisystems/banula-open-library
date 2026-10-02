package com.banula.openlib.ocpi.custom.energysuppliers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.banula.openlib.ocpi.custom.energysuppliers.validations.EnergySupplierValidator;

class EnergySupplierValidatorTest {

    private static final String ES_ID = "9900000000001";
    private static final String TENNET = "9911835000001";
    private static final String AMPRION = "4045399000077";
    private static final String MABIS = "DE0000000000000000000000000000001";
    private static final Set<String> KNOWN_TSOS = Set.of(TENNET, "3800000000001", AMPRION);

    private static EnergySupplier.EnergySupplierBuilder<?, ?> validSupplier() {
        return EnergySupplier.builder()
                .esMarketPartnerId(ES_ID)
                .name("Stadtwerke Test")
                .bkvMarketPartnerId("9900000000002")
                .balancingGroupId("11XDE-TSO---XYZ1")
                .bkzeReference("BKZE-1")
                .perTso(new ArrayList<>(List.of(tsoEntry(TENNET, "51709804123"))));
    }

    private static EnergySupplierTsoEntry tsoEntry(String tso, String malo) {
        return EnergySupplierTsoEntry.builder().tsoMarketPartnerId(tso).maloId(malo).mabisMeteringPoint(MABIS).build();
    }

    private static Map<String, String> validate(EnergySupplier supplier) {
        return EnergySupplierValidator.validate(ES_ID, EnergySupplierValidator.normalize(supplier), KNOWN_TSOS);
    }

    @Test
    void acceptsAValidSupplier() {
        assertTrue(validate(validSupplier().build()).isEmpty());
    }

    @Test
    void acceptsASupplierWithoutTsoEntries() {
        assertTrue(validate(validSupplier().perTso(null).build()).isEmpty());
    }

    @Test
    void normalisesCaseWhitespaceAndOwnership() {
        EnergySupplier normalized = EnergySupplierValidator.normalize(validSupplier()
                .name("  Stadtwerke Test ")
                .balancingGroupId(" 11xde-tso---xyz1 ")
                .perTso(new ArrayList<>(List.of(tsoEntry(TENNET, " 5170980412a ")))).build());

        assertEquals("Stadtwerke Test", normalized.getName());
        assertEquals("11XDE-TSO---XYZ1", normalized.getBalancingGroupId());
        assertEquals("5170980412A", normalized.getPerTso().get(0).getMaloId());
        assertEquals(MaloOwnerType.ES, normalized.getPerTso().get(0).getOwnerType());
    }

    @Test
    void rejectsMissingMandatoryFieldsFieldByField() {
        Map<String, String> errors = validate(validSupplier().name(" ").bkvMarketPartnerId(null)
                .balancingGroupId("short").build());

        assertTrue(errors.containsKey("name"));
        assertTrue(errors.containsKey("bkv_market_partner_id"));
        assertTrue(errors.containsKey("balancing_group_id"));
    }

    @Test
    void rejectsAMalformedKey() {
        Map<String, String> errors = EnergySupplierValidator.validate("12345",
                EnergySupplierValidator.normalize(validSupplier().esMarketPartnerId(null).build()), KNOWN_TSOS);

        assertTrue(errors.containsKey("es_market_partner_id"));
    }

    @Test
    void rejectsChangingTheKey() {
        Map<String, String> errors = validate(validSupplier().esMarketPartnerId("9900000000009").build());

        assertTrue(errors.get("es_market_partner_id").contains("cannot change"));
    }

    @Test
    void rejectsMoreThanFourTsoEntries() {
        List<EnergySupplierTsoEntry> five = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            five.add(tsoEntry(TENNET, "51709804123"));
        }

        assertTrue(validate(validSupplier().perTso(five).build()).containsKey("per_tso"));
    }

    @Test
    void rejectsAnUnknownTsoAndTheSameTsoTwice() {
        Map<String, String> errors = validate(validSupplier().perTso(new ArrayList<>(List.of(
                tsoEntry("0000000000000", "51709804123"),
                tsoEntry(TENNET, "51709804124"),
                tsoEntry(TENNET, "51709804125")))).build());

        assertTrue(errors.containsKey("per_tso[0].tso_market_partner_id"));
        assertTrue(errors.get("per_tso[2].tso_market_partner_id").contains("only be used once"));
    }

    @Test
    void rejectsMalformedMaloAndMabisMeteringPoint() {
        Map<String, String> errors = validate(validSupplier().perTso(new ArrayList<>(List.of(
                EnergySupplierTsoEntry.builder().tsoMarketPartnerId(AMPRION).maloId("123").mabisMeteringPoint("DE1")
                        .build()))).build());

        assertTrue(errors.containsKey("per_tso[0].malo_id"));
        assertTrue(errors.containsKey("per_tso[0].mabis_metering_point"));
    }
}
