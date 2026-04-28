package com.banula.openlib.ocpi.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.banula.openlib.ocpi.model.dto.TariffDTO;
import com.banula.openlib.ocpi.model.dto.TokenDTO;
import com.banula.openlib.ocpi.model.dto.EvseDTO;
import com.banula.openlib.ocpi.model.vo.EVSE;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RemovePartyFromIdAspect {

    @Around("@annotation(com.banula.openlib.ocpi.annotation.RemovePartyFromId)")
    public Object removePartyFromId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();

        String countryCode = null;
        String partyId = null;

        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String paramName = pathVariable.value().isEmpty() ? parameters[i].getName() : pathVariable.value();
                if ("countryCode".equals(paramName)) {
                    countryCode = (String) args[i];
                } else if ("partyId".equals(paramName)) {
                    partyId = (String) args[i];
                }
            }
        }

        if (countryCode != null && partyId != null) {
            String prefix = countryCode + "*" + partyId + "*";

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof LocationDTO) {
                    LocationDTO location = (LocationDTO) args[i];
                    location.setId(removePrefix(location.getId(), prefix));

                    if (location.getEvses() != null) {
                        for (EvseDTO evse : location.getEvses()) {
                            if (evse.getUid() != null) {
                                evse.setUid(removePrefix(evse.getUid(), prefix));
                            }
                            if (evse.getEvseId() != null) {
                                evse.setEvseId(removePrefix(evse.getEvseId(), prefix));
                            }
                        }
                    }
                } else if (args[i] instanceof EVSE) {
                    EVSE evse = (EVSE) args[i];
                    if (evse.getUid() != null) {
                        evse.setUid(removePrefix(evse.getUid(), prefix));
                    }
                    if (evse.getEvseId() != null) {
                        evse.setEvseId(removePrefix(evse.getEvseId(), prefix));
                    }
                } else if (args[i] instanceof TariffDTO) {
                    TariffDTO tariff = (TariffDTO) args[i];
                    if (tariff.getId() != null) {
                        tariff.setId(removePrefix(tariff.getId(), prefix));
                    }
                } else if (args[i] instanceof TokenDTO) {
                    TokenDTO token = (TokenDTO) args[i];
                    if (token.getUid() != null) {
                        token.setUid(removePrefix(token.getUid(), prefix));
                    }
                } else if (args[i] instanceof String) {
                    // Handle String path variables (locationId, tariffId, evseUid, etc.)
                    PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
                    if (pathVariable != null) {
                        String paramName = pathVariable.value().isEmpty() ? parameters[i].getName()
                                : pathVariable.value();
                        // Strip prefix from ID-related path variables
                        if ("locationId".equals(paramName) || "tariffId".equals(paramName) ||
                                "evseUid".equals(paramName) || "token_uid".equals(paramName)) {
                            args[i] = removePrefix((String) args[i], prefix);
                        }
                    }
                }
            }
        }

        return joinPoint.proceed(args);
    }

    private String removePrefix(String id, String prefix) {
        if (id != null && id.startsWith(prefix)) {
            String transformedId = id.substring(prefix.length());
            log.debug("Transformed ID from '{}' to '{}'", id, transformedId);
            return transformedId;
        }
        return id;
    }
}
