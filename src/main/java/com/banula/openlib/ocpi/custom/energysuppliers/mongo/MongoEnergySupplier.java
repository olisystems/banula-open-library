package com.banula.openlib.ocpi.custom.energysuppliers.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import com.banula.openlib.ocpi.custom.energysuppliers.EnergySupplier;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The stored form of an {@link EnergySupplier}, keyed by its market partner ID. Each service names the
 * collection through its {@code MongoCollectionMapper} bean ({@code getEnergySupplierCollectionName()}).
 */
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document("#{@MongoCollectionMapper.getEnergySupplierCollectionName()}")
public class MongoEnergySupplier extends EnergySupplier {
}
