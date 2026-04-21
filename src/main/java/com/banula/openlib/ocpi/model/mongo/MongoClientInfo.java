package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.ocpi.model.ClientInfo;
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
@Document("#{@MongoCollectionMapper.getHubClientInfoCollectionName()}")
@CompoundIndex(name = "unique_client_info", def = "{'countryCode': 1, 'partyId': 1, 'role': 1}", unique = true)
public class MongoClientInfo extends ClientInfo {

  @Id
  private String mongoId;

}
