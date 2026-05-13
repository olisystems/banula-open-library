package com.banula.openlib.ocpi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OCPILocalDateTimeDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ssZ")
    void testWithoutMillisWithZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32Z\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss")
    void testWithoutMillisWithoutZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss.SZ")
    void testWithTenthsWithZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32.2Z\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32, 200_000_000), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss.S")
    void testWithTenthsWithoutZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32.2\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32, 200_000_000), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss.SSSZ")
    void testWithMillisWithZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32.123Z\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32, 123_000_000), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss.SSSSSSZ (microseconds with Z)")
    void testWithNanoWithZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32.123456Z\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32, 123_456_000), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss.SSSSSS (microseconds without Z)")
    void testWithNanoWithOutZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32.123456\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32, 123_456_000), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should deserialize yyyy-MM-ddTHH:mm:ss.SSS")
    void testWithMillisWithoutZ() throws Exception {
        String json = "{\"timestamp\": \"2025-04-17T17:07:32.123\"}";
        TimestampWrapper wrapper = objectMapper.readValue(json, TimestampWrapper.class);
        assertEquals(LocalDateTime.of(2025, 4, 17, 17, 7, 32, 123_000_000), wrapper.getTimestamp());
    }

    @Test
    @DisplayName("Should throw exception on invalid format")
    void testInvalidFormat() {
        String json = "{\"timestamp\": \"17-04-2025 17:07:32\"}";
        assertThrows(Exception.class, () -> objectMapper.readValue(json, TimestampWrapper.class));
    }
}
