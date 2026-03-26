package com.banula.openlib.ocpi.annotation.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.banula.openlib.ocn.client.OcnClient;
import com.banula.openlib.ocn.model.OcnClientConfiguration;
import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.util.AuthenticationUtils;
import com.banula.openlib.ocpi.util.Constants;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AuthorizeHeaders {

    @Around("@annotation(com.banula.openlib.ocpi.annotation.AuthorizeHeaders)")
    public Object processAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String tokenB = request.getHeader("Authorization");
        String countryCode = request.getHeader("OCPI-to-country-code");
        String partyId = request.getHeader("OCPI-to-party-id");

        authorizeRequestHeaders(tokenB, partyId, countryCode, OcnClient.configuration);
        return joinPoint.proceed();
    }

    public static void authorizeRequestHeaders(String tokenB, String partyId, String countryCode,
            OcnClientConfiguration configuration) {
        if (tokenB == null
                || !AuthenticationUtils.extractToken(tokenB).equals(configuration.getTokenB())) {
            log.error("Invalid Token B: " + tokenB);
            throw new OCPICustomException("Invalid Token B.", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
        if (partyId == null || !partyId.equals(configuration.getFromPartyId())) {
            log.error("Invalid party id: " + partyId);
            throw new OCPICustomException("Invalid party id.", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
        if (countryCode == null || !countryCode.equals(configuration.getFromCountryCode())) {
            log.error("Invalid country code: " + countryCode);
            throw new OCPICustomException("Invalid country code", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
    }

}
