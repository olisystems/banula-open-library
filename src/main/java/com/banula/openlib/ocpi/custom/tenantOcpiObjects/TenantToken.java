package com.banula.openlib.ocpi.custom.tenantOcpiObjects;

import com.banula.openlib.ocpi.model.Token;
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
public class TenantToken extends Token {
    @JsonProperty("tenant")
    @NotEmpty(message = "Tenant cannot be empty")
    private String tenant;
}
