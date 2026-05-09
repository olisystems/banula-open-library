package com.banula.openlib.ocn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlatformResponse {
    private String token;
    private String versions;
}
