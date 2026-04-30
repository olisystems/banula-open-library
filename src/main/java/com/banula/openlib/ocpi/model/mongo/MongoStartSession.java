package com.banula.openlib.ocpi.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banula.openlib.ocpi.model.StartSession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor(force = true)
@Document("#{@MongoCollectionMapper.getStartSessionCollectionName()}")
@CompoundIndex(name = "unique_start_session_base", def = "{'locationId': 1, 'evseUid': 1, 'connectorId': 1, 'authorizationReference': 1, 'responseUrl': 1}", unique = true)
public class MongoStartSession extends StartSession {

    @Id
    private String mongoId;
}
