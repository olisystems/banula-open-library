package com.banula.openlib.ocpi.exception;

import com.banula.openlib.ocpi.util.Constants;
import lombok.Getter;

/**
 * CustomExceptionHandler represents an exception that can be thrown to handle
 * custom error scenarios.
 */
@Getter
public class OCPICustomException extends RuntimeException {
    private final String errorMessage;
    private final int statusCode;

    /**
     * Constructs a CustomExceptionHandler with the given error message, OCPI
     * status, and error code response.
     *
     * @param message    The error message to describe the exception.
     * @param statusCode The OCPI status code associated with the exception.
     */
    public OCPICustomException(String message, int statusCode) {
        super(message); // Pass message to parent constructor
        this.errorMessage = message;
        this.statusCode = statusCode;
    }

    public OCPICustomException(String message) {
        super(message); // Pass message to parent constructor
        this.errorMessage = message;
        this.statusCode = Constants.STATUS_CODE_GENERIC_SERVER_ERROR; // default OCPI status code = 3000, means internal
                                                                      // // server error
    }

}