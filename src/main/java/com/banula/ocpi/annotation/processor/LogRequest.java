package com.banula.ocpi.annotation.processor;

import jakarta.servlet.http.HttpServletRequest;
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
public class LogRequest {

    @Around("@annotation(com.banula.ocpi.annotation.LogRequest)")
    public Object processValidateLocation(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Controller called: {}", joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        for(Object arg: args){
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                log.info("HTTP Method: {} | Requested URL: {}", request.getMethod(), request.getRequestURL().toString());
            }
        }

        return joinPoint.proceed(args);
    }


}