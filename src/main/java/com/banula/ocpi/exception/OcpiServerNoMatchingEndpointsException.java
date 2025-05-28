package com.banula.ocpi.exception;

public class OcpiServerNoMatchingEndpointsException extends RuntimeException {
    public OcpiServerNoMatchingEndpointsException(String message) {
        super(message);
    }
}