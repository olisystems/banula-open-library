package com.banula.ocn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericOcnBody {
    @JsonProperty("id")
    public String id;
    @JsonProperty("city")
    public String city;
}
