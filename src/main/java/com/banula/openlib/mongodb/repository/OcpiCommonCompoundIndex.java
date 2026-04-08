package com.banula.openlib.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;

/**
 * Generic repository fragment for OCPI entities with standard country code,
 * party ID, and ID lookup.
 * This interface can be extended by any MongoRepository to add the standard
 * OCPI query method.
 * Spring Data MongoDB will automatically implement this method based on the
 * naming convention.
 * 
 * @param <T> The entity type
 */
public interface OcpiCommonCompoundIndex<T> {

    /**
     * Find an OCPI entity by its compound business key (countryCode + partyId +
     * id).
     * This is the standard OCPI pattern used across CPO, EMSP, and other OCPI
     * modules.
     * Spring Data will automatically generate the query implementation.
     * 
     * @param countryCode The country code (e.g., "DE", "NL")
     * @param partyId     The party identifier (e.g., "BAN", "ABC")
     * @param id          The entity's unique ID within the party's scope
     * @return Optional containing the found entity or empty if not found
     */
    @Query("{countryCode: ?0, partyId: ?1, id: ?2}")
    Optional<T> findByCompoundIndex(String countryCode, String partyId, String id);
}
