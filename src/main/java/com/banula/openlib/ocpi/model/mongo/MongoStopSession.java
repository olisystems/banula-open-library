package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
import com.banula.openlib.ocpi.model.StopSession;
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
@Document("#{@MongoCollectionMapper.getStopSessionCollectionName()}")
@CompoundIndex(name = "unique_stop_session", def = "{'countryCode': 1, 'partyId': 1, 'id': 1}", unique = true)
public class MongoStopSession extends StopSession {

    @Id
    private String mongoId;

}
