package com.banula.openlib.ocpi.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.banula.openlib.ocpi.util.EnumUtil;
import lombok.Getter;

@Getter
public enum AvailableFlexibility {
    NOT_AVAILABLE("NotAvailable"),
    CHARGING_ONLY("ChargingOnly"),
    MIXED_LOADS("MixedLoads"),
    INVALID("Invalid");

    private final String value;

    AvailableFlexibility(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    public static AvailableFlexibility fromValue(String value) {
        try {
            return EnumUtil.findByField(
                    AvailableFlexibility.class,
                    AvailableFlexibility::value,
                    value);
        } catch (Exception e) {
            return INVALID;
        }
    }

}
