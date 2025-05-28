package com.banula.openlib.ocn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcnRegistrationResponse {
    private String token;
    private String versions;
}
