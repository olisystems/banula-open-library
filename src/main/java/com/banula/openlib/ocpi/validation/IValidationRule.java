package com.banula.openlib.ocpi.validation;

import com.banula.openlib.ocpi.exception.PropertyConstraintException;

public interface IValidationRule {
    void validate(String value) throws PropertyConstraintException;
}
