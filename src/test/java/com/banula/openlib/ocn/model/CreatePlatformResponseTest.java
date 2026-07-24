package com.banula.openlib.ocn.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class CreatePlatformResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deserializes legacy flat token and versions fields")
    void legacyFlatFields() throws Exception {
        String json = """
                {
                  "token": "legacy-token",
                  "versions": "https://example.com/ocpi/versions"
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("legacy-token", response.getToken());
        assertEquals("https://example.com/ocpi/versions", response.getVersions());
    }

    @Test
    @DisplayName("Deserializes nested auth.tokenA and versionsUrl")
    void nestedAuthAndVersionsUrl() throws Exception {
        String json = """
                {
                  "auth": {
                    "tokenA": "nested-token-a",
                    "tokenB": "nested-token-b",
                    "tokenC": "nested-token-c",
                    "selfCredentialsToken": "self-token",
                    "handshakeSelfInitiated": true
                  },
                  "versionsUrl": "https://example.com/ocpi/versions-v2"
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("nested-token-a", response.getToken());
        assertEquals("https://example.com/ocpi/versions-v2", response.getVersions());
    }

    @Test
    @DisplayName("Falls back to nested values when legacy fields are blank")
    void blankLegacyFallsBackToNested() throws Exception {
        String json = """
                {
                  "token": "   ",
                  "versions": "",
                  "auth": {
                    "tokenA": "fallback-token-a"
                  },
                  "versionsUrl": "https://example.com/ocpi/versions-fallback"
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("fallback-token-a", response.getToken());
        assertEquals("https://example.com/ocpi/versions-fallback", response.getVersions());
    }

    @Test
    @DisplayName("Ignores unknown JSON properties")
    void ignoresUnknownProperties() throws Exception {
        String json = """
                {
                  "token": "legacy-token",
                  "versions": "https://example.com/ocpi/versions",
                  "unknownField": "should-be-ignored",
                  "auth": {
                    "tokenA": "nested-token-a",
                    "extraAuthField": 123
                  }
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("legacy-token", response.getToken());
        assertEquals("https://example.com/ocpi/versions", response.getVersions());
        assertEquals("nested-token-a", response.getAuth().getTokenA());
    }

    @Test
    @DisplayName("Prefers non-blank legacy fields over nested values")
    void prefersLegacyWhenPresent() throws Exception {
        String json = """
                {
                  "token": "legacy-token",
                  "versions": "https://example.com/ocpi/versions-legacy",
                  "auth": {
                    "tokenA": "nested-token-a"
                  },
                  "versionsUrl": "https://example.com/ocpi/versions-v2"
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("legacy-token", response.getToken());
        assertEquals("https://example.com/ocpi/versions-legacy", response.getVersions());
    }

    @Test
    @DisplayName("Returns null when no token or versions are available")
    void returnsNullWhenMissing() throws Exception {
        CreatePlatformResponse response = objectMapper.readValue("{}", CreatePlatformResponse.class);

        assertNull(response.getToken());
        assertNull(response.getVersions());
    }

    @Test
    @DisplayName("toString excludes credential fields")
    void toStringExcludesCredentials() {
        CreatePlatformResponse.Auth auth = new CreatePlatformResponse.Auth(
                "secret-token-a",
                "secret-token-b",
                "secret-token-c",
                "secret-self-token",
                true);
        CreatePlatformResponse response = new CreatePlatformResponse(
                "secret-token",
                "https://example.com/ocpi/versions",
                auth,
                "https://example.com/ocpi/versions-v2");

        String responseString = response.toString();
        String authString = auth.toString();

        assertFalse(responseString.contains("secret-token"));
        assertFalse(responseString.contains("secret-token-a"));
        assertFalse(responseString.contains("secret-self-token"));
        assertFalse(authString.contains("secret-token-a"));
        assertFalse(authString.contains("secret-token-b"));
        assertFalse(authString.contains("secret-token-c"));
        assertFalse(authString.contains("secret-self-token"));
    }
}
