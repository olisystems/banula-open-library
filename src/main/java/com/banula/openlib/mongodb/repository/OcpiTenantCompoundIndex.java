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
public interface OcpiTenantCompoundIndex<T> {

    /**
     * Find an OCPI entity by its compound business key (countryCode + partyId +
     * id).
     * This is the standard OCPI pattern used across CPO, EMSP, and other OCPI
     * modules.
     * The query is defined explicitly via {@code @Query} with positional
     * parameters.
     * 
     * @param tenant      The tenant id (e.g. "DE_ABC")
     * @param countryCode The country code (e.g., "DE", "NL")
     * @param partyId     The party identifier (e.g., "BAN", "ABC")
     * @param id          The entity's unique ID within the party's scope
     * @return Optional containing the found entity or empty if not found
     */
    @Query("{tenant: ?0, countryCode: ?1, partyId: ?2, id: ?3}")
    Optional<T> findByCompoundIndex(String tenant, String countryCode, String partyId, String id);

    @Query("{'_id': ?0, 'tenant': ?1}")
    Optional<T> findByMongoIdAndTenant(String mongoId, String tenant);

    @Query("{'_id': ?0, 'tenant': ?1}")
    Optional<T> findByObjectIdAndTenant(ObjectId objectId, String tenant);

    @Query("{'id': ?0, 'tenant': ?1}")
    List<T> findAllByIdAndTenant(String id, String tenant);

    /**
     * Tenant-scoped counterpart of
     * {@link OcpiCommonCompoundIndex#findByFlexibleId(String)}: resolve an entity
     * from the composite mongo {@code _id}, a raw ObjectId, or the plain OCPI
     * {@code id} field, trying each in turn within the given tenant.
     * <p>
     * The {@code id}-field fallback is what lets a caller holding only the bare
     * OCPI id find the document, which the composite {@code _id} lookup cannot do.
     *
     * @param id     the identifier in any of the supported forms
     * @param tenant the tenant the entity belongs to
     * @return the first matching entity within the tenant, or empty when none
     *         matches
     */
    default Optional<T> findByFlexibleId(String id, String tenant) {
        if (id == null) {
            return Optional.empty();
        }

        if (id.contains("*")) {
            String[] parts = id.split("\\*");
            if (parts.length == 3) {
                Optional<T> byCompoundIndex = findByCompoundIndex(tenant, parts[0], parts[1], parts[2]);
                if (byCompoundIndex.isPresent()) {
                    return byCompoundIndex;
                }
            }
        }

        Optional<T> byMongoId = findByMongoIdAndTenant(id, tenant);
        if (byMongoId.isPresent()) {
            return byMongoId;
        }

        if (ObjectId.isValid(id)) {
            Optional<T> byObjectId = findByObjectIdAndTenant(new ObjectId(id), tenant);
            if (byObjectId.isPresent()) {
                return byObjectId;
            }
        }

        List<T> byBusinessId = findAllByIdAndTenant(id, tenant);
        return byBusinessId.isEmpty() ? Optional.empty() : Optional.of(byBusinessId.get(0));
    }
}
