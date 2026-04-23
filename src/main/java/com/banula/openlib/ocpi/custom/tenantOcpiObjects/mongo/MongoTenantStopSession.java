package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoTenantOcpiCompositeId;
import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantStopSession;
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
@Document("#{@MongoCollectionMapper.getStopSessionCollectionName()}")
@CompoundIndex(name = "unique_stop_session", def = "{'countryCode': 1, 'partyId': 1, 'id': 1, 'tenant': 1}", unique = true)
public class MongoTenantStopSession extends TenantStopSession {

    @Id
    private String mongoId;

}
