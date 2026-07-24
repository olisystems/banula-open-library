package com.banula.openlib.ocn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePlatformResponse {
    @ToString.Exclude
    private Auth auth;
    private String versionsUrl;

    public String getTokenA() {
        return auth != null ? auth.getTokenA() : null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Auth {
        @ToString.Exclude
        private String tokenA;
        @ToString.Exclude
        private String tokenB;
        @ToString.Exclude
        private String tokenC;
        @ToString.Exclude
        private String selfCredentialsToken;
        private Boolean handshakeSelfInitiated;
    }
}
