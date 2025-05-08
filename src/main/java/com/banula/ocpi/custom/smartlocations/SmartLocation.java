package com.banula.ocpi.custom.smartlocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.Location;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmartLocation extends Location {
    @JsonProperty("default_supplier")
    @NotEmpty(message = "Default supplier cannot be empty")
    private String defaultSupplier;

    @JsonProperty("malo")
    @NotEmpty(message = "Malo cannot be empty")
    private String malo;

    @JsonProperty("smart_meter_id")
    @NotEmpty(message = "Smart meter ID cannot be empty")
    private String smartMeterId;

    @JsonProperty("message_queue_url")
    @NotEmpty(message = "Message queue URL cannot be empty")
    private String messageQueueUrl;
}
