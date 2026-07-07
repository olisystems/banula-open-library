package com.banula.openlib.ocpi.model;

import com.banula.openlib.ocpi.model.enums.ChargingProfileResultType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractProfileResult {

    @JsonProperty("result")
    protected ChargingProfileResultType result;

    @JsonIgnore
    public String type;

    public String getType() {
        return this.getClass().getSimpleName();
    }

}
