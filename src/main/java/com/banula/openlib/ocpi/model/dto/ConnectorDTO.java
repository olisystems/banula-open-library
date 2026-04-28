package com.banula.openlib.ocpi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ConnectorFormat;
import com.banula.openlib.ocpi.model.enums.ConnectorType;
import com.banula.openlib.ocpi.model.enums.PowerType;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorDTO {

    @NotNull(message = "id must not be null")
    @Size(max = 36, message = "id must be at most 36 characters")
    @JsonProperty("id")
    private String id;

    @NotNull(message = "standard must not be null")
    @JsonProperty("standard")
    private ConnectorType standard;

    @NotNull(message = "format must not be null")
    @JsonProperty("format")
    private ConnectorFormat format;

    @NotNull(message = "power_type must not be null")
    @JsonProperty("power_type")
    private PowerType powerType;

    @NotNull(message = "max_voltage must not be null")
    @JsonProperty("max_voltage")
    private Integer maxVoltage;

    @NotNull(message = "max_amperage must not be null")
    @JsonProperty("max_amperage")
    private Integer maxAmperage;

    @NotNull(message = "max_electric_power is a required field in Banula style of Charging")
    @JsonProperty("max_electric_power")
    private Integer maxElectricPower;

    @Size(max = 36, message = "tariff_ids must be at most 36 characters")
    @NotNull(message = "tariff_ids is a required field in Banula style of Charging")
    @JsonProperty("tariff_ids")
    private List<@NotEmpty(message = "tariff_id must not be empty") String> tariffIds;

    @Size(max = 255, message = "terms_and_conditions must be at most 255 characters")
    @JsonProperty("terms_and_conditions")
    private String termsAndConditions;

    @NotNull(message = "last_updated must not be null")
    @JsonProperty("last_updated")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;
}
