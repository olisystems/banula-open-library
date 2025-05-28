package com.banula.ocpi.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.vo.ChargingProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetChargingProfileRequestDTO {
    @NotNull
    @JsonProperty("charging_profile")
    private ChargingProfile chargingProfile;
    @NotBlank
    @JsonProperty("response_url ")
    private String responseUrl;
}
