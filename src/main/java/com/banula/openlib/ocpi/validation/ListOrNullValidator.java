package com.banula.openlib.ocpi.validation;

import com.banula.openlib.ocpi.exception.PropertyConstraintException;

import java.util.List;

public class ListOrNullValidator extends Validator<List> {

    private final String ERROR_MESSAGE = "The following field must be a List or Null: ";

    @Override
    public void validate(List value, String fieldName) throws PropertyConstraintException {
        if (value != null && !(value instanceof List)) {
            throw new PropertyConstraintException(value, ERROR_MESSAGE + fieldName);
        }
    }

    @Override
    public void validate(List value) throws PropertyConstraintException {
        if (value != null && !(value instanceof List)) {
            throw new PropertyConstraintException(value, ERROR_MESSAGE + "unknown");
        }
    }

}
