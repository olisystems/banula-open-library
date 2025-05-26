package com.banula.openlib.ocpi.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargingRequestDto {

    private String connectorId;
    private Double requiredEnergy;
    private Double timeAvailability;
    private String paymentIntentId;

}
