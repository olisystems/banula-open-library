package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.ocpi.model.CDR;
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
@Document("#{@MongoCollectionMapper.getCdrCollectionName()}")
@CompoundIndex(name = "unique_cdr", def = "{'countryCode': 1, 'partyId': 1, 'id': 1}", unique = true)
public class MongoCDR extends CDR {

    @Id
    private String mongoId;
}
