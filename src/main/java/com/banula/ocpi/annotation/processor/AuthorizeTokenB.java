package com.banula.ocpi.annotation.processor;

import com.banula.ocn.client.OcnClient;
import com.banula.ocpi.util.AuthenticationUtils;
import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuthorizeTokenB {

    private final OcnClient ocnClient;

    //TODO: We should revisit the token B handling later to understand how the OCN node implemented the ocpis credentials module.
    @Around("@annotation(com.banula.ocpi.annotation.AuthorizeTokenB)")
    public Object processAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenB = request.getHeader("Authorization");
        authorizeRequestToken(tokenB);
        return joinPoint.proceed();
    }

    private void authorizeRequestToken(String tokenB) {
        if(tokenB == null || !AuthenticationUtils.extractToken(tokenB).equals(ocnClient.getConfiguration().getTokenB())) {
            log.error("Invalid Token B: " + tokenB);
            throw new OCPICustomException("Invalid Token B.", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
    }

}
