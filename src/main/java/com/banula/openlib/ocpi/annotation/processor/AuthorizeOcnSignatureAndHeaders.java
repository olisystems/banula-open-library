package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocn.Notary;
import com.banula.openlib.ocn.client.OcnClient;
import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.banula.openlib.ocpi.annotation.processor.AuthorizeHeaders.authorizeRequestHeaders;

/**
 * This class is responsible for authorizing the OCN signature and headers. This
 * notation is to be used only for methods that will exclusivelly be called by
 * the OCN node, as it will check the OCN signature and headers.
 */

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuthorizeOcnSignatureAndHeaders {

    private final OcnClient ocnClient;

    @Around("@annotation(com.banula.openlib.ocpi.annotation.AuthorizeOcnSignatureAndHeaders)")
    public Object processAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String tokenB = request.getHeader("Authorization");
        String countryCode = request.getHeader("OCPI-to-country-code");
        String partyId = request.getHeader("OCPI-to-party-id");

        if (ocnClient.getConfiguration().isSigningSupported()) {
            String ocnSignature = request.getHeader("OCN-Signature");
            if (ocnSignature == null) {
                log.error("OCN Signature is missing.");
                throw new OCPICustomException("OCN Signature is missing.",
                        Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
            }
            Notary notary = Notary.deserialize(ocnSignature);
            boolean isVerified = notary.verifySignature();
            if (!isVerified) {
                log.error("Invalid OCN Signature: " + ocnSignature);
                throw new OCPICustomException("Invalid OCN Signature.",
                        Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
            }
        }

        authorizeRequestHeaders(tokenB, partyId, countryCode, ocnClient);
        return joinPoint.proceed();
    }

}
