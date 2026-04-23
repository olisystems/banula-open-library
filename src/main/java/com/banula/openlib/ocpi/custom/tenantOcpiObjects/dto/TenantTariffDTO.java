package com.banula.openlib.ocpi.custom.tenantOcpiObjects.dto;

import com.banula.openlib.ocpi.model.dto.TariffDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TenantTariffDTO extends TariffDTO {
    @JsonProperty("tenant")
    private String tenant;
}
