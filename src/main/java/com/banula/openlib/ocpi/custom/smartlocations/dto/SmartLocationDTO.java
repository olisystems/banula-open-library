package com.banula.openlib.ocpi.custom.smartlocations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.custom.smartlocations.validations.SmartLocationCreateGroup;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SmartLocationDTO extends LocationDTO {
    @JsonProperty("default_supplier")
    @NotEmpty(message = "Default supplier cannot be empty", groups = SmartLocationCreateGroup.class)
    private String defaultSupplier;

    @JsonProperty("malo")
    @NotEmpty(message = "Malo cannot be empty", groups = SmartLocationCreateGroup.class)
    private String malo;

    @JsonProperty("smart_meter_id")
    @NotEmpty(message = "Smart meter ID cannot be empty", groups = SmartLocationCreateGroup.class)
    private String smartMeterId;

    @JsonProperty("message_queue_url")
    @NotEmpty(message = "Message queue URL cannot be empty", groups = SmartLocationCreateGroup.class)
    private String messageQueueUrl;
}
