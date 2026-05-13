package com.banula.openlib.ocpi.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banula.openlib.ocpi.model.StopSession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor(force = true)
@Document("#{@MongoCollectionMapper.getStopSessionCollectionName()}")
@CompoundIndex(name = "unique_base_stop_session", def = "{'sessionId': 1, 'responseUrl': 1}", unique = true)
public class MongoStopSession extends StopSession {

    @Id
    private String mongoId;

}
