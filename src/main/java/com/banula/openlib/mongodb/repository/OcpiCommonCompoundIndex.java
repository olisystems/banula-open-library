package com.banula.openlib.mongodb.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

/**
 * Generic repository fragment for OCPI entities with standard country code,
 * party ID, and ID lookup.
 * This interface can be extended by any MongoRepository to add the standard
 * OCPI query method.
 * The query method is implemented via an explicit {@code @Query} annotation.
 * 
 * @param <T> The entity type
 */
public interface OcpiCommonCompoundIndex<T> {

    /**
     * Find an OCPI entity by its compound business key (countryCode + partyId +
     * id).
     * This is the standard OCPI pattern used across CPO, EMSP, and other OCPI
     * modules.
     * The query is defined explicitly via {@code @Query} with positional
     * parameters.
     * 
     * @param countryCode The country code (e.g., "DE", "NL")
     * @param partyId     The party identifier (e.g., "BAN", "ABC")
     * @param id          The entity's unique ID within the party's scope
     * @return Optional containing the found entity or empty if not found
     */
    @Query("{countryCode: ?0, partyId: ?1, id: ?2}")
    Optional<T> findByCompoundIndex(String countryCode, String partyId, String id);

    @Query("{'_id': ?0}")
    Optional<T> findByMongoId(String mongoId);

    @Query("{'_id': ?0}")
    Optional<T> findByObjectId(ObjectId objectId);

    default Optional<T> findByFlexibleId(String id) {
        if (id != null && id.contains("*")) {
            String[] parts = id.split("\\*");
            if (parts.length == 3) {
                return findByCompoundIndex(parts[0], parts[1], parts[2]);
            }
        }
        Optional<T> result = findByMongoId(id);
        if (result.isEmpty() && ObjectId.isValid(id)) {
            return findByObjectId(new ObjectId(id));
        }
        return result;
    }
}
