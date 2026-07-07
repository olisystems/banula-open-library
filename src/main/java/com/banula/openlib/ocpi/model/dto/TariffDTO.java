package com.banula.openlib.ocpi.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.banula.openlib.ocpi.model.enums.TariffType;
import com.banula.openlib.ocpi.model.vo.DisplayText;
import com.banula.openlib.ocpi.model.vo.EnergyMix;
import com.banula.openlib.ocpi.model.vo.Price;
import com.banula.openlib.ocpi.model.vo.TariffElement;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TariffDTO {

    @Size(min = 1, max = 2)
    @JsonProperty("country_code")
    private String countryCode;

    @Size(min = 1, max = 3)
    @JsonProperty("party_id")
    private String partyId;

    @Size(min = 1, max = 36)
    private String id;
    @NotEmpty
    @Size(min = 1, max = 3)
    private String currency;
    private TariffType type;
    @JsonProperty("tariff_alt_text")
    private List<DisplayText> tariffAltText;
    @JsonProperty("tariff_alt_url")
    private String tariffAltUrl;
    @JsonProperty("min_price")
    @Valid
    private Price minPrice;
    @Valid
    @JsonProperty("max_price")
    private Price maxPrice;
    @NotNull
    @Valid
    private List<TariffElement> elements;
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;
    @JsonProperty("end_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;
    @JsonProperty("energy_mix")
    @Valid
    // TODO verify how to require this directly in the tariff manager later to be
    // banula compliant, by now it should be OCPI Compliant and not to deny a Tariff
    // with this field empty
    // @NotNull(message = "energy_mix is a required field in Banula Style of
    // Charging.")
    private EnergyMix energyMix;

    @NotNull(message = "Last updated time cannot be null")
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
