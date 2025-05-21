package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.ChargingProfileResultType;

public abstract class AbstractProfileResult {

    @JsonProperty("result")
    protected ChargingProfileResultType result;

    @JsonIgnore
    public String type;

    public String getType() {
        return this.getClass().getSimpleName();
    }

}
