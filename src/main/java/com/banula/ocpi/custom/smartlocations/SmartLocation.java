package com.banula.ocpi.custom.smartlocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.Location;
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
public class SmartLocation extends Location {
   @JsonProperty("market_location_id")
   @NotEmpty(message = "Marktlokation-ID cannot be empty")
   private String marketLocationId;

   @JsonProperty("metering_location_id")
   @NotEmpty(message = "Messlokation-ID cannot be empty")
   private String meteringLocationId;

   @JsonProperty("dso_market_partner_id")
   @NotEmpty(message = "Verteilnetzbetreiber Partner-ID cannot be empty")
   private String dsoMarketPartnerId;

   @JsonProperty("tso_market_partner_id")
   @NotEmpty(message = "Übertragungsnetzbetreiber Partner-ID cannot be empty")
   private String tsoMarketPartnerId;

   @JsonProperty("mpo_market_partner_id")
   @NotEmpty(message = "Messstellenbetreiber Partner-ID cannot be empty")
   private String mpoMarketPartnerId;

   @JsonProperty("metering_data_source")
   @NotNull(message = "Metering data source cannot be null")
   private MeteringDataSource meteringDataSource;

   @JsonProperty("smart_meter_id")
   private String smartMeterId;

   @JsonProperty("message_queue_url")
   private String messageQueueUrl;

   @JsonProperty("default_supplier")
   @NotNull(message = "Default supplier cannot be null")
   private DefaultSupplier defaultSupplier;
}
