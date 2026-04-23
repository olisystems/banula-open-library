package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
import com.banula.openlib.ocpi.model.vo.CommandResultResponse;
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
@Document("#{@MongoCollectionMapper.getCommandResultResponseCollectionName()}")
@CompoundIndex(name = "unique_command_result", def = "{'countryCode': 1, 'partyId': 1, 'uid': 1}", unique = true)
public class MongoCommandResultResponse extends CommandResultResponse {

    @Id
    private String mongoId;

}
