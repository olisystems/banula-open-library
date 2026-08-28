package com.banula.openlib.ocn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePlatformResponse {
    /** Legacy flat token field (older node responses). */
    @ToString.Exclude
    private String token;
    /** Legacy flat versions URL field (older node responses). */
    private String versions;
    /** Nested auth from ocn-node-v2 PlatformEntity responses. */
    @ToString.Exclude
    private Auth auth;
    /** versionsUrl from ocn-node-v2 PlatformEntity responses. */
    private String versionsUrl;

    /** Compatibility constructor matching the published (token, versions) API. */
    public CreatePlatformResponse(String token, String versions) {
        this.token = token;
        this.versions = versions;
    }

    /**
     * Token A from nested {@code auth.tokenA}, falling back to the legacy flat
     * {@code token} when auth is absent or its tokenA is blank.
     */
    public String getTokenA() {
        if (auth != null) {
            String nested = auth.getTokenA();
            if (nested != null && !nested.isBlank()) {
                return nested;
            }
        }
        return token;
    }

    /**
     * Versions URL from nested {@code versionsUrl}, falling back to the legacy
     * flat {@code versions} field when the nested value is absent or blank.
     */
    public String getVersionsUrl() {
        if (versionsUrl != null && !versionsUrl.isBlank()) {
            return versionsUrl;
        }
        return versions;
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
