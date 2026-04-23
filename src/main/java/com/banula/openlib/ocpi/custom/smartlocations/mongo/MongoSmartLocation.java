package com.banula.openlib.ocpi.custom.smartlocations.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
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
@NoArgsConstructor(force = true)
@Document("#{@MongoCollectionMapper.getSmartLocationCollectionName()}")
@CompoundIndex(name = "unique_location", def = "{'countryCode': 1, 'partyId': 1, 'id': 1}", unique = true)
public class MongoSmartLocation extends SmartLocation implements HasMongoOcpiCompositeId {

    @Id
    private String mongoId;

    public String getOcpiId() {
        return getId();
    }

}
