package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * AOP aspect for
 * {@link com.banula.openlib.ocpi.annotation.OcpiPatchCompositeId}.
 *
 * <p>
 * Complies with OCPI 2.2.1 PATCH receiver endpoints (Sessions §3.2.1, Locations
 * §3.1.2,
 * Tokens §4.1.2) where the composite ID fields (country_code, party_id, id/uid)
 * may be
 * absent from the request body since they are already carried in the URL path.
 *
 * <p>
 * Expected method signature (positional):
 * 
 * <pre>
 *   args[0] = DTO (ChargingSessionDTO, LocationDTO, TokenDTO, …)
 *   args[1] = String countryCode  (path variable)
 *   args[2] = String partyId      (path variable)
 *   args[3] = String id           (path variable — may be named sessionId, locationId, etc.)
 * </pre>
 *
 * <p>
 * Behaviour per field:
 * <ul>
 * <li>If the DTO field is null or empty → set it from the path variable.</li>
 * <li>If the DTO field is already set → validate it matches the path variable;
 * throw {@link OCPICustomException} (status 2001) with a descriptive message if
 * not.</li>
 * </ul>
 *
 * <p>
 * The id field is resolved generically: the aspect first looks for a field
 * named {@code id};
 * if absent it falls back to {@code uid} (used by the Token model).
 */
@Slf4j
@Aspect
@Component
public class OcpiPatchCompositeIdAspect {

    @Around("@annotation(com.banula.openlib.ocpi.annotation.OcpiPatchCompositeId)")
    public Object applyPatchCompositeId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object dto = args[0];

        // 1. Validate countryCode and partyId (always at index 1 and 2)
        String pathCountryCode = (String) args[1];
        String pathPartyId = (String) args[2];
        validatePathValue("countryCode", pathCountryCode, 2);
        validatePathValue("partyId", pathPartyId, 3);

        // 2. Determine mode based on path parameter presence
        // Normal (Location/Session/Token): args[1]=CC, args[2]=PID, args[3]=ID
        // EVSE: args[1]=CC, args[2]=PID, args[3]=LID, args[4]=EUID
        // Connector: args[1]=CC, args[2]=PID, args[3]=LID, args[4]=EUID, args[5]=CID

        if (args.length >= 6 && args[5] instanceof String) {
            // Connector Mode: args[5] is the Connector ID
            String pathConnectorId = (String) args[5];
            fillOrValidate(dto, "id", "id", pathConnectorId);
        } else if (args.length >= 5 && args[4] instanceof String) {
            // EVSE Mode: args[4] is the EVSE UID
            String pathEvseUid = (String) args[4];
            fillOrValidate(dto, "uid", "uid", pathEvseUid);
        } else {
            // Base Mode (Location/Session/Token/etc)
            String pathId = (String) args[3];

            fillOrValidate(dto, "countryCode", "country_code", pathCountryCode);
            fillOrValidate(dto, "partyId", "party_id", pathPartyId);

            String idFieldName = hasField(dto, "id") ? "id" : "uid";
            fillOrValidate(dto, idFieldName, idFieldName, pathId);
        }

        return joinPoint.proceed(args);
    }

    private void fillOrValidate(Object dto, String fieldName, String jsonName, String pathValue) {
        String dtoValue = (String) getField(dto, fieldName);
        if (dtoValue == null || dtoValue.isEmpty()) {
            setField(dto, fieldName, pathValue);
        } else if (!dtoValue.equals(pathValue)) {
            String message = String.format(
                    "Payload field '%s' value '%s' does not match path variable value '%s'",
                    jsonName, dtoValue, pathValue);
            log.error(message);
            throw new OCPICustomException(message, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }
    }

    private void validatePathValue(String name, String value, Integer expectedLength) {
        if (value == null || value.isEmpty()) {
            String message = String.format("Path variable '%s' is missing or empty", name);
            log.error(message);
            throw new OCPICustomException(message, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        if (expectedLength != null && value.length() != expectedLength) {
            String message = String.format("Path variable '%s' value '%s' has invalid length. Expected length: %d",
                    name, value, expectedLength);
            log.error(message);
            throw new OCPICustomException(message, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }
    }

    private boolean hasField(Object obj, String fieldName) {
        return findField(obj.getClass(), fieldName) != null;
    }

    private Object getField(Object obj, String fieldName) {
        try {
            Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            log.warn("Could not read field '{}' from {}: {}", fieldName, obj.getClass().getSimpleName(),
                    e.getMessage());
        }
        return null;
    }

    private void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            log.warn("Could not set field '{}' on {}: {}", fieldName, obj.getClass().getSimpleName(), e.getMessage());
        }
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
