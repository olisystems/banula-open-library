package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantTariff;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Document("#{@MongoCollectionMapper.getTariffCollectionName()}")
@CompoundIndex(name = "unique_tariff", def = "{'countryCode': 1, 'partyId': 1, 'id': 1}", unique = true)
public class MongoTenantTariff extends TenantTariff {

    @Id
    private String mongoId;
}
