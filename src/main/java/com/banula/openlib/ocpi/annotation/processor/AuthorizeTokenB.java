package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocn.client.OcnClient;
import com.banula.openlib.ocpi.util.AuthenticationUtils;
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

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuthorizeTokenB {

    private final OcnClient ocnClient;

    @Around("@annotation(com.banula.openlib.ocpi.annotation.AuthorizeTokenB)")
    public Object processAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String tokenB = request.getHeader("Authorization");
        authorizeRequestToken(tokenB);
        return joinPoint.proceed();
    }

    private void authorizeRequestToken(String tokenB) {
        if (tokenB == null
                || !AuthenticationUtils.extractToken(tokenB).equals(ocnClient.getConfiguration().getTokenB())) {
            log.error("Invalid Token B: " + tokenB);
            throw new OCPICustomException("Invalid Token B.", Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
    }

}
