package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.vo.ChargingProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SetChargingProfile {

    /**
     * Contains limits for the available power or current over time.
     */
    @JsonProperty("charging_profile")
    @NotNull // Ensures that the charging profile is not null
    @Valid
    private ChargingProfile chargingProfile;

    /**
     * URL that the ChargingProfileResult POST should be sent to. This URL might contain a unique ID to be able
     * to distinguish between GET ActiveChargingProfile requests.
     */
    @JsonProperty("response_url")
    @NotEmpty // Ensures that the response URL is not blank
    @Size(max = 255) // Ensures that the response URL does not exceed 255 characters
    private String responseUrl;

    public void setChargingProfile(ChargingProfile chargingProfile) {
        this.chargingProfile = chargingProfile;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }
}
