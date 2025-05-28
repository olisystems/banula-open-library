package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.validation.ValidationRules;
import com.banula.openlib.ocpi.validation.Validator;
import com.banula.openlib.ocpi.validation.ValidatorBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public abstract class AbstractCommand {

    @JsonIgnore
    protected final transient Validator responseUrlValidator = new ValidatorBuilder()
            .setRequired(true)
            .addRule(ValidationRules.string255())
            .build();

    /**
     * URL that the CommandResult POST should be send to. This URL might
     * contain an unique ID to be able to distinguish between ReserveNow
     * requests.
     */
    @NotBlank
    @Size(min = 1, max = 255)
    @JsonProperty("response_url")
    protected String responseUrl;

    @JsonIgnore
    public String type;

    public String getType() {
        return this.getClass().getSimpleName();
    }
}