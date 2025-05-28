package com.banula.openlib.ocpi.model.dto.response;

import com.banula.openlib.ocpi.model.enums.ChargingProfileResponseType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingProfileResponseDTO {
    @NotNull
    private ChargingProfileResponseType result;
    @NotNull
    private Integer timeout;
}
