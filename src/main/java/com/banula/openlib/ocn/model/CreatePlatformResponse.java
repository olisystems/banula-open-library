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

    /** Compatibility constructor for legacy flat response fields. */
    public CreatePlatformResponse(String token, String versions) {
        this.token = token;
        this.versions = versions;
    }

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
