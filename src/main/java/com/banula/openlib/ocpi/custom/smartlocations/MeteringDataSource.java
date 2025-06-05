package com.banula.openlib.ocpi.custom.smartlocations;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MeteringDataSource {
    @JsonProperty("MSCONS")
    MSCONS,

    @JsonProperty("SMART_METER")
    SMART_METER,

    @JsonProperty("CONTROL_BACKEND")
    CONTROL_BACKEND
}