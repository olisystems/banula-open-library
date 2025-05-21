package com.banula.ocn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResult {
    boolean isValid;

    String error;

    public VerifyResult(boolean isValid) {
        this.isValid = isValid;
    }
}
