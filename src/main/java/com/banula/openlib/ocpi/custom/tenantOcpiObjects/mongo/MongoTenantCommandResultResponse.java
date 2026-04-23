package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoTenantOcpiCompositeId;
import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantCommandResultResponse;
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
@Document("#{@MongoCollectionMapper.getCommandResultResponseCollectionName()}")
@CompoundIndex(name = "unique_command_result", def = "{'countryCode': 1, 'partyId': 1, 'uid': 1, 'tenant': 1}", unique = true)
public class MongoTenantCommandResultResponse extends TenantCommandResultResponse {

    @Id
    private String mongoId;

    public String getOcpiUid() {
        return getUid();
    }
}
