package com.banula.openlib.mongodb.interfaces;

public interface HasMongoOcpiCompositeId {
    String getCountryCode();

    String getPartyId();

    String getOcpiId(); // this is the "internal" id like CDR id, Location id, etc.

    String getMongoId();

    void setMongoId(String id);
}
