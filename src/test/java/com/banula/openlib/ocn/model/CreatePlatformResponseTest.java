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
    @DisplayName("Deserializes auth.tokenA and versionsUrl")
    void deserializesPlatformEntityShape() throws Exception {
        String json = """
                {
                  "auth": {
                    "tokenA": "nested-token-a",
                    "tokenB": "nested-token-b",
                    "tokenC": "nested-token-c",
                    "selfCredentialsToken": "self-token",
                    "handshakeSelfInitiated": true
                  },
                  "versionsUrl": "https://example.com/ocpi/versions"
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("nested-token-a", response.getTokenA());
        assertEquals("https://example.com/ocpi/versions", response.getVersionsUrl());
        assertEquals(true, response.getAuth().getHandshakeSelfInitiated());
    }

    @Test
    @DisplayName("Ignores unknown JSON properties")
    void ignoresUnknownProperties() throws Exception {
        String json = """
                {
                  "unknownField": "should-be-ignored",
                  "auth": {
                    "tokenA": "nested-token-a",
                    "extraAuthField": 123
                  },
                  "versionsUrl": "https://example.com/ocpi/versions"
                }
                """;

        CreatePlatformResponse response = objectMapper.readValue(json, CreatePlatformResponse.class);

        assertEquals("nested-token-a", response.getTokenA());
        assertEquals("https://example.com/ocpi/versions", response.getVersionsUrl());
    }

    @Test
    @DisplayName("Returns null when auth or versionsUrl are missing")
    void returnsNullWhenMissing() throws Exception {
        CreatePlatformResponse response = objectMapper.readValue("{}", CreatePlatformResponse.class);

        assertNull(response.getTokenA());
        assertNull(response.getVersionsUrl());
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
                auth,
                "https://example.com/ocpi/versions");

        String responseString = response.toString();
        String authString = auth.toString();

        assertFalse(responseString.contains("secret-token-a"));
        assertFalse(responseString.contains("secret-self-token"));
        assertFalse(authString.contains("secret-token-a"));
        assertFalse(authString.contains("secret-token-b"));
        assertFalse(authString.contains("secret-token-c"));
        assertFalse(authString.contains("secret-self-token"));
    }
}
