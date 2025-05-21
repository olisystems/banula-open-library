package com.banula.ocpi.annotation.processor;

import com.banula.ocn.client.OcnClient;
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
public class AuthorizeIntegrationTest {

    private final OcnClient ocnClient;

    @Around("@annotation(com.banula.ocpi.model.AuthorizeIntegrationTest)")
    public Object processAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String integrationTest = request.getHeader("Integration-Test");
        authorizeRequest(integrationTest);
        return joinPoint.proceed();
    }

    private void authorizeRequest(String integrationTest) {
        String integrationTestConfig = ocnClient.getConfiguration().getIntegrationTestParameter();
        if (integrationTest == null || !integrationTest.equals(integrationTestConfig)) {
            log.error("Invalid Integration Test header parameter: " + integrationTest);
            throw new OCPICustomException("Invalid Integration Test header parameter.",
                    Constants.STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE);
        }
    }

}
