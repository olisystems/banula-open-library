package com.banula.openlib.ocpi.validation;

import com.banula.openlib.ocpi.exception.PropertyConstraintException;

import java.util.List;

public class ListOfAtLeastOneObjects extends Validator<List> {

    private final String ERROR_MESSAGE = "List should have at least one object, field name: ";

    @Override
    public void validate(List value, String fieldName) throws PropertyConstraintException {
        if (value.isEmpty()) {
            throw new PropertyConstraintException(value, ERROR_MESSAGE + fieldName);
        }
    }

    public void validate(List value) throws PropertyConstraintException {
        if (value.isEmpty()) {
            throw new PropertyConstraintException(value, ERROR_MESSAGE + "unknown");
        }
    }
}
