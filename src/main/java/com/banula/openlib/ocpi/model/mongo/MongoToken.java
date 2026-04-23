package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
import com.banula.openlib.ocpi.model.Token;
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
@Document("#{@MongoCollectionMapper.getTokenCollectionName()}")
@CompoundIndex(name = "unique_token", def = "{'countryCode': 1, 'partyId': 1, 'uid': 1}", unique = true)
public class MongoToken extends Token implements HasMongoOcpiCompositeId {

    @Id
    private String mongoId;

    @Override
    public String getOcpiId() {
        return getUid();
    }
}
