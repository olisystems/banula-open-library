package com.banula.ocpi.validation;

import com.banula.ocpi.exception.PropertyConstraintException;

public abstract class Validator<T> {

    public boolean safeValidate(T value, String fieldName) {
        boolean returnValue = true;
        try {
            this.validate(value, fieldName);
        } catch (Exception ex) {
            returnValue = false;
        }
        return returnValue;
    }

    public boolean safeValidate(T value) {
        boolean returnValue = true;
        try {
            this.validate(value);
        } catch (Exception ex) {
            returnValue = false;
        }
        return returnValue;
    }

    public abstract void validate(T value, String fieldName) throws PropertyConstraintException;

    public abstract void validate(T value) throws PropertyConstraintException;

}
