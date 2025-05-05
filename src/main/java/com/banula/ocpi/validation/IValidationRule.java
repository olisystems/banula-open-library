package com.banula.ocpi.validation;

import com.banula.ocpi.exception.PropertyConstraintException;

public interface IValidationRule {
    void validate(String value) throws PropertyConstraintException;
}
