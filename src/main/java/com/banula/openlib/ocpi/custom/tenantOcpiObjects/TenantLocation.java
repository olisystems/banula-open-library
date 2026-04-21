package com.banula.openlib.ocpi.custom.tenantOcpiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.Location;
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
public class TenantLocation extends Location {
    @JsonProperty("tenant_id")
    @NotEmpty(message = "Tenant ID cannot be empty")
    private String tenantId;
}
