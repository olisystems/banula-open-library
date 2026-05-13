package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banula.openlib.mongodb.interfaces.HasMongoTenantOcpiCompositeId;
import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantLocation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Document("#{@MongoCollectionMapper.getLocationCollectionName()}")
@CompoundIndex(name = "unique_tenant_location", def = "{'countryCode': 1, 'partyId': 1, 'id': 1, 'tenant': 1}", unique = true)
public class MongoTenantLocation extends TenantLocation implements HasMongoTenantOcpiCompositeId {

    @Id
    private String mongoId;

    public String getOcpiId() {
        return getId();
    }
}
