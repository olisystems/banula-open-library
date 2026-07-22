package com.banula.openlib.ocn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePlatformResponse {
    /** Legacy flat token field (older node responses). */
    private String token;
    /** Legacy flat versions URL field (older node responses). */
    private String versions;
    /** Nested auth from ocn-node-v2 PlatformEntity responses. */
    private Auth auth;
    /** versionsUrl from ocn-node-v2 PlatformEntity responses. */
    private String versionsUrl;

    public String getToken() {
        if (token != null && !token.isBlank()) {
            return token;
        }
        return auth != null ? auth.getTokenA() : null;
    }

    public String getVersions() {
        if (versions != null && !versions.isBlank()) {
            return versions;
        }
        return versionsUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Auth {
        private String tokenA;
        private String tokenB;
        private String tokenC;
        private String selfCredentialsToken;
        private Boolean handshakeSelfInitiated;
    }
}
