package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.ChargingProfileResponseType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The ChargingProfileResponse object is sent in the HTTP response body.
 */
@Data
@ToString
@NoArgsConstructor
public class ChargingProfileResponse {

    /**
     * Response from the CPO on the ChargingProfile request.
     */
    @NotNull(message = "Result must not be null")
    @JsonProperty("result")
    private ChargingProfileResponseType result;

    /**
     * Timeout for this ChargingProfile request in seconds. When the Result is not received within this timeout,
     * the eMSP can assume that the message might never be sent.
     */
    @JsonProperty("timeout")
    @NotNull(message = "Result must not be null")
    private Integer timeout;

}
