package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.banula.openlib.ocpi.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ValidateLocation {

    @Around("@annotation(com.banula.openlib.ocpi.annotation.ValidateLocation)")
    public Object processValidateLocation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        LocationDTO locationDTO = (LocationDTO) args[0];
        String countryCode = (String) args[1];
        String party_id = (String) args[2];
        String locationId = (String) args[3];

        // validate location required path is equal to the object passed
        if (!locationDTO.getCountryCode().equals(countryCode)) {
            String errorMessage = "Country code in the path variable does not match the country code in the object";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }
        if (!locationDTO.getPartyId().equals(party_id)) {
            String errorMessage = "Party ID in the path variable does not match the party ID in the object";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }
        if (!locationDTO.getId().equals(locationId)) {
            String errorMessage = "Location.id in the path variable does not match the Location.id in the object";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        // verify lastUpdated field
        if (locationDTO.getLastUpdated() == null) {
            String errorMessage = "Location lastUpdated field is missing";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        return joinPoint.proceed(args);
    }

}