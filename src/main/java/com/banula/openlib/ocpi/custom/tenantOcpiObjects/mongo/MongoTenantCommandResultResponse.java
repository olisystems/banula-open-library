package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantCommandResultResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Document("#{@MongoCollectionMapper.getCommandResultResponseCollectionName()}")
@CompoundIndex(name = "unique_tenant_command_result", def = "{'uid': 1, 'tenant': 1}", unique = true)
public class MongoTenantCommandResultResponse extends TenantCommandResultResponse {

    @Id
    private String mongoId;

    public String getOcpiUid() {
        return getUid();
    }
}
