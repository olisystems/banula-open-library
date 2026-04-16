package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.custom.smartlocations.DefaultSupplier;
import com.banula.openlib.ocpi.custom.smartlocations.MeteringDataSource;
import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.model.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelPatcherUtilTest {

    @Test
    void smartLocationPatcher_updatesInheritedAndSubclassFields() throws IllegalAccessException {
        SmartLocation existing = buildSmartLocation(false, "OLD_MALO", "OLD_SMART_METER");
        SmartLocation incomplete = new SmartLocation();
        incomplete.setPublish(true);
        incomplete.setMalo("NEW_MALO");
        incomplete.setSmartMeterId("NEW_SMART_METER");
        incomplete.setMeteringDataSource(MeteringDataSource.CONTROL_BACKEND);
        incomplete.setDefaultSupplier(DefaultSupplier.builder()
                .supplierMarketPartnerId("SUPPLIER")
                .bkvId("BKV")
                .balancingGroupEicId("BAL")
                .defaultSupplierMaloId("DEFAULT_MALO")
                .build());

        ModelPatcherUtil.smartLocationPatcher(existing, incomplete);

        assertTrue(existing.getPublish());
        assertEquals("NEW_MALO", existing.getMalo());
        assertEquals("NEW_SMART_METER", existing.getSmartMeterId());
        assertEquals(MeteringDataSource.CONTROL_BACKEND, existing.getMeteringDataSource());
        assertEquals("SUPPLIER", existing.getDefaultSupplier().getSupplierMarketPartnerId());
    }

    @Test
    void locationPatcher_updatesPublishField() throws IllegalAccessException {
        Location existing = new Location();
        existing.setPublish(false);
        existing.setName("Original");

        Location incomplete = new Location();
        incomplete.setPublish(true);

        ModelPatcherUtil.locationPatcher(existing, incomplete);

        assertTrue(existing.getPublish());
        assertEquals("Original", existing.getName());
    }

    private SmartLocation buildSmartLocation(boolean publish, String malo, String smartMeterId) {
        SmartLocation location = new SmartLocation();
        location.setCountryCode("DE");
        location.setPartyId("ABC");
        location.setId("LOCSIMUL");
        location.setPublish(publish);
        location.setPublishAllowedTo(List.of());
        location.setName("Sample");
        location.setAddress("Main Street 1");
        location.setCity("Berlin");
        location.setCountry("DEU");
        location.setTimeZone("Europe/Berlin");
        location.setMalo(malo);
        location.setSmartMeterId(smartMeterId);
        location.setMessageQueueUrl("http://mq");
        location.setMarketLocationId("MARKET");
        location.setMeteringLocationId("METER");
        location.setDsoMarketPartnerId("DSO");
        location.setTsoMarketPartnerId("TSO");
        location.setMpoMarketPartnerId("MPO");
        location.setMeteringDataSource(MeteringDataSource.MSCONS);
        location.setDefaultSupplier(DefaultSupplier.builder()
                .supplierMarketPartnerId("OLD_SUPPLIER")
                .bkvId("OLD_BKV")
                .balancingGroupEicId("OLD_BAL")
                .build());
        return location;
    }
}
