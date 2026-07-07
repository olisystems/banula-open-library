package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDomainObject {
    @JsonIgnore
    public String type;

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
