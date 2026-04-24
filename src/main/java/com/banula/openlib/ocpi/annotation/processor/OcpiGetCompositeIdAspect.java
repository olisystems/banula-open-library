package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP aspect for {@link com.banula.openlib.ocpi.annotation.OcpiGetCompositeId}.
 *
 * <p>
 * Used on GET OCPI receiver endpoints. This aspect validates that the path
 * variables (countryCode, partyId, and id) are present and not empty.
 *
 * <p>
 * Expected method signature (positional):
 * 
 * <pre>
 *   args[0] = String countryCode  (path variable)
 *   args[1] = String partyId      (path variable)
 *   args[2] = String id           (path variable)
 *   ... (optional additional path variables like evseUid, connectorId)
 * </pre>
 */
@Slf4j
@Aspect
@Component
public class OcpiGetCompositeIdAspect {

    @Around("@annotation(com.banula.openlib.ocpi.annotation.OcpiGetCompositeId)")
    public Object validateGetPath(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if (args.length < 3) {
            String message = "OcpiGetCompositeId requires at least 3 path variables (countryCode, partyId, id)";
            log.error(message);
            throw new OCPICustomException(message, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        validatePathValue("countryCode", (String) args[0], 2);
        validatePathValue("partyId", (String) args[1], 3);
        validatePathValue("id", (String) args[2], null);

        return joinPoint.proceed(args);
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
}
