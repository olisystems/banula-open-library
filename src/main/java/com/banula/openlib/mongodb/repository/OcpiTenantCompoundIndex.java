package com.banula.openlib.mongodb.repository;

import java.util.Optional;

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
}
