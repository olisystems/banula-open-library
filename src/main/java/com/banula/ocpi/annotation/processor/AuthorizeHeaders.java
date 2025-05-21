package com.banula.ocpi.annotation.processor;

import com.banula.ocn.client.OcnClient;
import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import com.banula.ocpi.util.AuthenticationUtils;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuthorizeHeaders {

    private final OcnClient ocnClient;

    @Around("@annotation(com.banula.ocpi.annotation.AuthorizeHeaders)")
    public Object processAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenB = request.getHeader("Authorization");
        String countryCode = request.getHeader("OCPI-to-country-code");
        String partyId = request.getHeader("OCPI-to-party-id");

        authorizeRequestHeaders(tokenB, partyId, countryCode, ocnClient);
        return joinPoint.proceed();
    }

    //TODO: We should revisit the token B handling later to understand how the OCN node implemented the ocpis credentials module.
    public static void authorizeRequestHeaders(String tokenB, String partyId, String countryCode, OcnClient ocnClient) {
        if(tokenB == null || !AuthenticationUtils.extractToken(tokenB).equals(ocnClient.getConfiguration().getTokenB())) {
            log.error("Invalid Token B: " + tokenB);
            throw new OCPICustomException("Invalid Token B.", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
        if(partyId == null || !partyId.equals(ocnClient.getConfiguration().getFromPartyId())) {
            log.error("Invalid party id: " + partyId);
            throw new OCPICustomException("Invalid party id.", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
        if(countryCode == null || !countryCode.equals(ocnClient.getConfiguration().getFromCountryCode())) {
            log.error("Invalid country code: " + countryCode);
            throw new OCPICustomException("Invalid country code", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
    }

}
