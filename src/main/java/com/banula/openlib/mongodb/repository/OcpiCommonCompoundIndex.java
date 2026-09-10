package com.banula.openlib.mongodb.repository;

import java.util.List;
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

    @Query("{'id': ?0}")
    List<T> findAllByBusinessId(String id);

    /**
     * Resolve an entity from whichever form of identifier the caller happens to
     * hold: the composite mongo {@code _id} ("DE*ABC*&lt;id&gt;"), a raw ObjectId,
     * or the plain OCPI {@code id} field.
     * <p>
     * Each form is tried in turn and an empty result falls through to the next.
     * The {@code id}-field lookup matters most: entities are persisted under a
     * composite {@code _id}, so looking one up by the bare OCPI id it was given by
     * another service (a CDR id from the CPO, say) matches no {@code _id} at all
     * and previously returned empty even though the document was there.
     * <p>
     * Falling through also fixes ids that themselves contain {@code *} (session ids
     * such as "SIMUL1*1*5"), which the compound-key branch would otherwise read as
     * a country/party/id triple and fail on.
     *
     * @param id the identifier in any of the supported forms
     * @return the first matching entity, or empty when none matches. An OCPI id is
     *         unique within a party but may repeat across parties; pass the
     *         composite form when that distinction matters.
     */
    default Optional<T> findByFlexibleId(String id) {
        if (id == null) {
            return Optional.empty();
        }

        if (id.contains("*")) {
            String[] parts = id.split("\\*");
            if (parts.length == 3) {
                Optional<T> byCompoundIndex = findByCompoundIndex(parts[0], parts[1], parts[2]);
                if (byCompoundIndex.isPresent()) {
                    return byCompoundIndex;
                }
            }
        }

        Optional<T> byMongoId = findByMongoId(id);
        if (byMongoId.isPresent()) {
            return byMongoId;
        }

        if (ObjectId.isValid(id)) {
            Optional<T> byObjectId = findByObjectId(new ObjectId(id));
            if (byObjectId.isPresent()) {
                return byObjectId;
            }
        }

        List<T> byBusinessId = findAllByBusinessId(id);
        return byBusinessId.isEmpty() ? Optional.empty() : Optional.of(byBusinessId.get(0));
    }
}
