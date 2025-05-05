package com.banula.ocpi.model;

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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tariff {

    /**
     * ISO-3166 alpha-2 country code of the CPO that owns this Tariff.
     */
    @JsonProperty("country_code")
    @NotEmpty(message = "Country code cannot be empty.")
    @Size(min = 2, max = 2, message = "Country code must be between 1 and 2 characters long")
    private String countryCode;

    /**
     * ID of the CPO that 'owns' this Tariff (following the ISO-15118 standard).
     */
    @JsonProperty("party_id")
    @NotEmpty(message = "Party ID cannot be empty.")
    @Size(min = 3, max = 3, message = "Party ID must be between 1 and 3 characters long")
    private String partyId;

    /**
     * Uniquely identifies the tariff within the CPO’s platform (and suboperator platforms).
     */
    @JsonProperty("id")
    @NotEmpty(message = "Tariff ID cannot be empty.")
    private String id;

    /**
     * Defines the type of the tariff.
     */
    @JsonProperty("type")
    private TariffType type;

    /**
     * Currency of the tariff.
     */
    @JsonProperty("currency")
    @NotEmpty(message = "Currency cannot be empty.")
    private String currency;

    /**
     * List of multi-language alternative tariff info texts.
     */
    @JsonProperty("tariff_alt_text")
    private List<DisplayText> tariffAltText;

    /**
     * URL to a web page that contains an explanation of the tariff information in human readable form.
     */
    @JsonProperty("tariff_alt_url")
    private String tariffAltUrl;

    /**
     * Minimum price of the tariff.
     */
    @JsonProperty("min_price")
    @Valid
    private Price minPrice;

    /**
     * Maximum price of the tariff.
     */
    @JsonProperty("max_price")
    @Valid
    private Price maxPrice;

    /**
     * List of Tariff Elements.
     */
    @JsonProperty("elements")
    @NotNull(message = "Tariff elements cannot be empty.")
    private List<TariffElement> elements;

    /**
     * The time when this tariff becomes active.
     */
    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    /**
     * The time after which this tariff is no longer valid.
     */
    @JsonProperty("end_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    /**
     * Details on the energy supplied with this tariff.
     */
    @JsonProperty("energy_mix")
    @Valid
    private EnergyMix energyMix;

    /**
     * Timestamp when this Tariff was last updated.
     */
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
