package com.banula.openlib.ocpi.custom.tenantOcpiObjects;

import com.banula.openlib.ocpi.model.Tariff;
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
public class TenantTariff extends Tariff {
    private String tenant;
}
