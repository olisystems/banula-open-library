package com.banula.ocpi.validation;

import com.banula.ocpi.exception.PropertyConstraintException;

public class RequiredValidator extends Validator<Object> {
    private final String ERROR_MESSAGE = "Field is required and must not be Null, field name: ";

    @Override
    public void validate(Object value) throws PropertyConstraintException {
        if (value == null) {
            throw new PropertyConstraintException(ERROR_MESSAGE + "unknown", null);
        }
    }

    public void validate(Object value, String fieldName) throws PropertyConstraintException {
        if (value == null) {
            throw new PropertyConstraintException(ERROR_MESSAGE + fieldName, null);
        }
    }
}
