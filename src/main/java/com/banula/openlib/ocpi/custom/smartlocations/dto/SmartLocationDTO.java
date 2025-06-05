package com.banula.openlib.ocpi.custom.smartlocations.dto;

import com.banula.openlib.ocpi.custom.smartlocations.DefaultSupplier;
import com.banula.openlib.ocpi.custom.smartlocations.MeteringDataSource;
import com.banula.openlib.ocpi.custom.smartlocations.validations.SmartLocationCreateGroup;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SmartLocationDTO extends LocationDTO {
    @JsonProperty("market_location_id")
    @NotEmpty(message = "Marktlokation-ID cannot be empty", groups = SmartLocationCreateGroup.class)
    private String marketLocationId;

    @JsonProperty("metering_location_id")
    @NotEmpty(message = "Messlokation-ID cannot be empty", groups = SmartLocationCreateGroup.class)
    private String meteringLocationId;

    @JsonProperty("dso_market_partner_id")
    @NotEmpty(message = "Verteilnetzbetreiber Partner-ID cannot be empty", groups = SmartLocationCreateGroup.class)
    private String dsoMarketPartnerId;

    @JsonProperty("tso_market_partner_id")
    @NotEmpty(message = "Übertragungsnetzbetreiber Partner-ID cannot be empty", groups = SmartLocationCreateGroup.class)
    private String tsoMarketPartnerId;

    @JsonProperty("mpo_market_partner_id")
    @NotEmpty(message = "Messstellenbetreiber Partner-ID cannot be empty", groups = SmartLocationCreateGroup.class)
    private String mpoMarketPartnerId;

    @JsonProperty("metering_data_source")
    @NotNull(message = "Metering data source cannot be null", groups = SmartLocationCreateGroup.class)
    private MeteringDataSource meteringDataSource;

    @JsonProperty("malo")
    @NotEmpty(message = "Malo cannot be empty", groups = SmartLocationCreateGroup.class)
    private String malo;

    @JsonProperty("smart_meter_id")
    private String smartMeterId;

    @JsonProperty("message_queue_url")
    private String messageQueueUrl;

    @Valid
    @JsonProperty("default_supplier")
    @NotNull(message = "Default supplier cannot be null", groups = SmartLocationCreateGroup.class)
    private DefaultSupplier defaultSupplier;
}