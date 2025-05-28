package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.ChargingProfileResultType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ClearProfileResult object is sent by the CPO to the given response_url in a POST request.
 * It contains the result of the DELETE (ClearProfile) request sent by the eMSP
 */
@Data
@NoArgsConstructor
public class ClearProfileResult {

    /**
     * The result of the DELETE (ClearProfile) request sent by the eMSP.
     */
    @NotNull(message = "Result must not be null")
    @JsonProperty("result")
    private ChargingProfileResultType result;

}
