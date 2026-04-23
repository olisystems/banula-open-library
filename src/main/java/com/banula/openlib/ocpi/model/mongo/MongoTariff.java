package com.banula.openlib.ocpi.model.mongo;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
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
public class MongoTariff extends Tariff implements HasMongoOcpiCompositeId {

    @Id
    private String mongoId;

    public String getOcpiId() {
        return getId();
    }
}
