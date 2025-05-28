package com.banula.openlib.ocn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewriteVerifyResult {
    boolean isValid;
    String error;
    ValuesToSign<?> previousValues;

    public RewriteVerifyResult(boolean isValid, String error) {
        this.isValid = isValid;
        this.error = error;
    }
}
