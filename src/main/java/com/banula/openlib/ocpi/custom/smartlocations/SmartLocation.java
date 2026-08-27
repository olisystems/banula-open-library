package com.banula.openlib.ocpi.custom.smartlocations;

import com.banula.openlib.ocpi.model.Location;
import com.banula.openlib.ocpi.util.OCPILocalDateDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

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
    private String marketLocationId;

    @JsonProperty("metering_location_id")
    private String meteringLocationId;

    @JsonProperty("dso_market_partner_id")
    private String dsoMarketPartnerId;

    @JsonProperty("tso_market_partner_id")
    private String tsoMarketPartnerId;

    @JsonProperty("mpo_market_partner_id")
    private String mpoMarketPartnerId;

    @JsonProperty("metering_data_source")
    private MeteringDataSource meteringDataSource;

    @JsonProperty("malo")
    private String malo;

    @JsonProperty("smart_meter_id")
    private String smartMeterId;

    @JsonProperty("message_queue_url")
    private String messageQueueUrl;

    @JsonProperty("default_supplier")
    private DefaultSupplier defaultSupplier;

    @JsonProperty("smart_location_state")
    private SmartLocationState smartLocationState;

    @JsonProperty("active_first_day")
    @JsonSerialize(using = OCPILocalDateSerializer.class)
    @JsonDeserialize(using = OCPILocalDateDeserializer.class)
    private LocalDate activeFirstDay;

    @JsonProperty("active_last_day")
    @JsonSerialize(using = OCPILocalDateSerializer.class)
    @JsonDeserialize(using = OCPILocalDateDeserializer.class)
    private LocalDate activeLastDay;
}
