package com.banula.openlib.ocpi.exception;

import lombok.Getter;

/**
 * An outflow request was answered with an OCPI error, either by the platform or
 * by a party behind it. The upstream status_code is kept, so
 * GlobalExceptionHandler hands the same code back to this service's caller
 * instead of the request looking successful.
 */
@Getter
public class OcpiUpstreamException extends OCPICustomException {
    /** HTTP status of the platform's response. */
    private final int httpStatus;

    public OcpiUpstreamException(String message, int statusCode, int httpStatus) {
        super(message, statusCode);
        this.httpStatus = httpStatus;
    }
}
