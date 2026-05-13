package com.banula.openlib.ocpi.custom.tenantOcpiObjects;

import com.banula.openlib.ocpi.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @JsonProperty("tenant")
    @NotEmpty(message = "Tenant id cannot be empty")
    @Size(min = 6, max = 6, message = "Tenant id must be exactly 6 characters. Ex: DE_ABC")
    private String tenant;
}
