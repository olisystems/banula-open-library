package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import com.banula.ocpi.validation.RequiredValidator;
import com.banula.ocpi.validation.Validatable;
import com.banula.ocpi.validation.Validator;

import java.time.LocalDateTime;

public abstract class ResponseFormat implements Validatable {

    @JsonIgnore
    private final Validator requiredValidator = new RequiredValidator();

    @JsonProperty("status_code")
    protected Integer statusCode;
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    protected LocalDateTime timestamp;
    @JsonProperty("status_message")
    protected String statusMessage;

    @Override
    public boolean validate() {
        return requiredValidator.safeValidate(statusCode)
                && requiredValidator.safeValidate(timestamp);
    }
}
