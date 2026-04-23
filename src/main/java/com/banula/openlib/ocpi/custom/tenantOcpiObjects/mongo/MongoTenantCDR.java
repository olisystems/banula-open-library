package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
import com.banula.openlib.mongodb.interfaces.HasMongoTenantOcpiCompositeId;
import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantCDR;
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
@Document("#{@MongoCollectionMapper.getCdrCollectionName()}")
@CompoundIndex(name = "unique_cdr", def = "{'countryCode': 1, 'partyId': 1, 'id': 1, 'tenant': 1}", unique = true)
public class MongoTenantCDR extends TenantCDR implements HasMongoTenantOcpiCompositeId {

    @Id
    private String mongoId;

    public String getOcpiId() {
        return getId();
    }
}
