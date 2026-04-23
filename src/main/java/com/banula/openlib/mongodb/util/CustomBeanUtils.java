package com.banula.openlib.mongodb.util;

import org.springframework.beans.BeanUtils;

import com.banula.openlib.mongodb.interfaces.HasMongoOcpiCompositeId;
import com.banula.openlib.mongodb.interfaces.HasMongoTenantOcpiCompositeId;

public class CustomBeanUtils {

    public static void copyProperties(Object origin, Object destination) {
        BeanUtils.copyProperties(origin, destination);

        if (destination instanceof HasMongoTenantOcpiCompositeId tenantEntity) {
            handleTenantOcpiCompositeId(tenantEntity);
        } else if (destination instanceof HasMongoOcpiCompositeId ocpiEntity) {
            handleOcpiCompositeId(ocpiEntity);
        }
    }

    private static void handleTenantOcpiCompositeId(HasMongoTenantOcpiCompositeId entity) {
        if (entity.getTenant() == null || entity.getCountryCode() == null ||
                entity.getPartyId() == null || entity.getOcpiId() == null) {
            throw new IllegalArgumentException(String.format(
                    "Missing fields for Tenant OCPI Composite ID: tenant=%s, countryCode=%s, partyId=%s, id=%s",
                    entity.getTenant(), entity.getCountryCode(), entity.getPartyId(), entity.getOcpiId()));
        }

        String mongoId = String.format("%s*%s*%s*%s",
                entity.getTenant(),
                entity.getCountryCode(),
                entity.getPartyId(),
                entity.getOcpiId());
        entity.setMongoId(mongoId);
    }

    private static void handleOcpiCompositeId(HasMongoOcpiCompositeId entity) {
        if (entity.getCountryCode() == null || entity.getPartyId() == null || entity.getOcpiId() == null) {
            throw new IllegalArgumentException(String.format(
                    "Missing fields for OCPI Composite ID: countryCode=%s, partyId=%s, id=%s",
                    entity.getCountryCode(), entity.getPartyId(), entity.getOcpiId()));
        }

        String mongoId = String.format("%s*%s*%s",
                entity.getCountryCode(),
                entity.getPartyId(),
                entity.getOcpiId());
        entity.setMongoId(mongoId);
    }
}
