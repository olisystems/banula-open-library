package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.ocpi.model.Tariff;
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
@Document("#{@MongoCollectionMapper.getTariffCollectionName()}")
@CompoundIndex(name = "unique_tariff", def = "{'countryCode': 1, 'partyId': 1, 'id': 1}", unique = true)
public class MongoTariff extends Tariff {

    @Id
    private String mongoId;
}
