package com.banula.ocpi.handler;

import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.model.OcpiErrorResponse;
import com.banula.ocpi.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Component
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value =  {OCPICustomException.class})
    public ResponseEntity<OcpiErrorResponse> handleCustomExceptionHandler(OCPICustomException e) {
        return ResponseEntity.ok().body(new OcpiErrorResponse(e.getErrorMessage(), e.getStatusCode()));
    }

    @ExceptionHandler(value =  {HttpMessageNotReadableException.class})
    public ResponseEntity<OcpiErrorResponse> handleCustomExceptionHandler(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(new OcpiErrorResponse(e.getMessage(), Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS));
    }

    @ExceptionHandler(value =  {MethodArgumentNotValidException.class})
    public ResponseEntity<OcpiErrorResponse> handleCustomExceptionHandler(MethodArgumentNotValidException e) {
        String errorResponse = Arrays.toString(e.getDetailMessageArguments())
                .replace("[], ", ""); //for empty element removal
        return ResponseEntity.ok().body(new OcpiErrorResponse(errorResponse, Constants.STATUS_CODE_INVALID_OR_MISSING_PARAMETERS));
    }



}
