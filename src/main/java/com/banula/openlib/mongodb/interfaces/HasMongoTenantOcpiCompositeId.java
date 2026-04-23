package com.banula.openlib.mongodb.interfaces;

public interface HasMongoTenantOcpiCompositeId extends HasMongoOcpiCompositeId {
    String getTenant();

    String getCountryCode();

    String getPartyId();

    String getOcpiId(); // this is the "internal" id like CDR id, Location id, etc.

    String getMongoId();

    void setMongoId(String id);
}
