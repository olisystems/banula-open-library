package com.banula.openlib.ocpi.custom.energysuppliers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.banula.openlib.ocpi.custom.energysuppliers.mapper.EnergySupplierMapper;
import com.banula.openlib.ocpi.custom.energysuppliers.mongo.MongoEnergySupplier;

class EnergySupplierMapperTest {

    private static EnergySupplier supplier() {
        return EnergySupplier.builder()
                .esMarketPartnerId("9900000000001")
                .name("Stadtwerke Test")
                .bkvMarketPartnerId("9900000000002")
                .balancingGroupId("11XDE-TSO---XYZB")
                .status(EnergySupplierStatus.ACTIVE)
                .perTso(new ArrayList<>(List.of(EnergySupplierTsoEntry.builder()
                        .tsoMarketPartnerId("9911835000001").maloId("51709804123")
                        .mabisMeteringPoint("DE0000000000000000000000000000001").ownerType(MaloOwnerType.ES).build())))
                .createdAt(Instant.parse("2026-10-01T08:00:00Z"))
                .build();
    }

    @Test
    void roundTripsThroughTheStoredForm() {
        EnergySupplier original = supplier();

        MongoEnergySupplier stored = EnergySupplierMapper.toMongo(original);
        EnergySupplier back = EnergySupplierMapper.toModel(stored);

        assertEquals(original, back);
        assertEquals(EnergySupplier.class, back.getClass());
    }

    @Test
    void copiesTheTsoEntriesInsteadOfSharingThem() {
        EnergySupplier original = supplier();

        MongoEnergySupplier stored = EnergySupplierMapper.toMongo(original);
        stored.getPerTso().get(0).setMaloId("CHANGED0000");

        assertNotSame(original.getPerTso(), stored.getPerTso());
        assertEquals("51709804123", original.getPerTso().get(0).getMaloId());
    }

    @Test
    void findsTheEntryForATsoControlArea() {
        EnergySupplier supplier = supplier();

        assertTrue(supplier.findTsoEntry("9911835000001").isPresent());
        assertTrue(supplier.findTsoEntry("4045399000077").isEmpty());
        assertFalse(supplier.findTsoEntry(null).isPresent());
    }
}
