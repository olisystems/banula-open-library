package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Information about an energy contract that belongs to a Token so a driver
 * could use his/her own energy contract
 * when charging at a Charge Point.
 */
@Data
@ToString
@NoArgsConstructor
public class EnergyContract {

    /**
     * Name of the energy supplier for this token.
     */
    @JsonProperty("supplier_name")
    @NotEmpty(message = "supplierName cannot be empty.")
    @Size(max = 64, message = "supplierName longer than 64 characters")
    private String supplierName;

    /**
     * ID of the energy contract.
     */
    @JsonProperty("contract_id")
    @Size(max = 64, message = "contractId longer than 64 characters")
    private String contractId;

}
