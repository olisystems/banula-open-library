package com.banula.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.model.enums.TariffType;
import com.banula.ocpi.model.vo.DisplayText;
import com.banula.ocpi.model.vo.EnergyMix;
import com.banula.ocpi.model.vo.Price;
import com.banula.ocpi.model.vo.TariffElement;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private EnergyMix energyMix;

    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
