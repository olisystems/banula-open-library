package com.banula.ocpi.annotation.processor;

import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.model.vo.Connector;
import com.banula.ocpi.util.Constants;
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
public class ValidateConnector {

    @Around("@annotation(com.olisystems.ocpi.annotation.ValidateConnector)")
    public Object processValidateEvse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Connector connector = (Connector) args[0];
        String connectorId = (String) args[5];


        // validate Connector Object and path variable
        if(!connector.getId().equals(connectorId)){
            String errorMessage = "Connector in the path variable does not match the Connector Id in the object";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        // verify lastUpdated field
        if(connector.getLastUpdated() == null){
            String errorMessage = "Connector lastUpdated field is missing";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        return joinPoint.proceed(args);
    }


}