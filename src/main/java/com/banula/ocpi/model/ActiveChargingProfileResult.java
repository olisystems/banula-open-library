package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.vo.ActiveChargingProfile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ActiveChargingProfileResult object is sent by the CPO to the given response_url in a POST request.
 * It contains the result of the GET (ActiveChargingProfile) request sent by the eMSP.
 */
@Data
@NoArgsConstructor
public class ActiveChargingProfileResult {

    /**
     * The requested ActiveChargingProfile, if the result field is set to: ACCEPTED
     */
    @NotNull(message = "Profile must not be null")
    @JsonProperty("profile")
    private ActiveChargingProfile profile;

}
