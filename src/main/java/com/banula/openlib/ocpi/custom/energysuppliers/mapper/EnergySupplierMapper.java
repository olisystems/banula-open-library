package com.banula.openlib.ocpi.custom.energysuppliers.mapper;

import java.util.ArrayList;
import java.util.List;

import com.banula.openlib.ocpi.custom.energysuppliers.EnergySupplier;
import com.banula.openlib.ocpi.custom.energysuppliers.EnergySupplierTsoEntry;
import com.banula.openlib.ocpi.custom.energysuppliers.mongo.MongoEnergySupplier;

/**
 * Converts between the API model and the stored form of an energy supplier. The TSO entries are copied,
 * so neither side shares a list with the other.
 */
public class EnergySupplierMapper {

    private EnergySupplierMapper() {
    }

    public static MongoEnergySupplier toMongo(EnergySupplier energySupplier) {
        if (energySupplier == null) {
            return null;
        }
        return MongoEnergySupplier.builder()
                .esMarketPartnerId(energySupplier.getEsMarketPartnerId())
                .name(energySupplier.getName())
                .bkvMarketPartnerId(energySupplier.getBkvMarketPartnerId())
                .balancingGroupId(energySupplier.getBalancingGroupId())
                .bkzeReference(energySupplier.getBkzeReference())
                .status(energySupplier.getStatus())
                .perTso(copy(energySupplier.getPerTso()))
                .createdAt(energySupplier.getCreatedAt())
                .updatedAt(energySupplier.getUpdatedAt())
                .syncedAt(energySupplier.getSyncedAt())
                .build();
    }

    public static EnergySupplier toModel(EnergySupplier stored) {
        if (stored == null) {
            return null;
        }
        return EnergySupplier.builder()
                .esMarketPartnerId(stored.getEsMarketPartnerId())
                .name(stored.getName())
                .bkvMarketPartnerId(stored.getBkvMarketPartnerId())
                .balancingGroupId(stored.getBalancingGroupId())
                .bkzeReference(stored.getBkzeReference())
                .status(stored.getStatus())
                .perTso(copy(stored.getPerTso()))
                .createdAt(stored.getCreatedAt())
                .updatedAt(stored.getUpdatedAt())
                .syncedAt(stored.getSyncedAt())
                .build();
    }

    private static List<EnergySupplierTsoEntry> copy(List<EnergySupplierTsoEntry> entries) {
        if (entries == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entries.stream()
                .map(entry -> entry == null ? null : entry.toBuilder().build())
                .toList());
    }
}
