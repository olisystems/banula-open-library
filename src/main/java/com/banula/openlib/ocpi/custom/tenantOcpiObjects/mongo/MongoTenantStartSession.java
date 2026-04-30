package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantStartSession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Document("#{@MongoCollectionMapper.getStartSessionCollectionName()}")
@CompoundIndex(name = "unique_start_session", def = "{'locationId': 1, 'evseUid': 1, 'connectorId': 1, 'authorizationReference': 1, 'responseUrl': 1, 'tenant': 1}", unique = true)
public class MongoTenantStartSession extends TenantStartSession {

    @Id
    private String mongoId;

}
