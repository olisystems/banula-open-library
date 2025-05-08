package com.banula.ocpi.annotation.processor;

import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.model.vo.EVSE;
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
public class ValidateEVSE {

    @Around("@annotation(com.olisystems.ocpi.annotation.ValidateEVSE)")
    public Object processValidateEvse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        EVSE evse = (EVSE) args[0];
        String evseUid = (String) args[4];


        // validate EVSE Object and path variable
        if(!evse.getUid().equals(evseUid)){
            String errorMessage = "EVSE in the path variable does not match the EVSE uid in the object";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        // verify lastUpdated field
        if(evse.getLastUpdated() == null){
            String errorMessage = "EVSE lastUpdated field is missing";
            log.error(errorMessage);
            throw new OCPICustomException(errorMessage, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS);
        }

        return joinPoint.proceed(args);
    }


}