package com.banula.openlib.ocpi.custom.tenantOcpiObjects.dto;

import com.banula.openlib.ocpi.custom.tenantOcpiObjects.validations.TenantLocationCreateGroup;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
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
public class TenantLocationDTO extends LocationDTO {
    @JsonProperty("tenant_id")
    @NotEmpty(message = "Tenant ID cannot be empty", groups = TenantLocationCreateGroup.class)
    private String tenantId;
}
