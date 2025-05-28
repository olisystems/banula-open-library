package com.banula.ocpi.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class OCPILocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter OFFSET_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String raw = p.getText().trim();

        // Try with offset (e.g., "Z" or "+00:00")
        try {
            OffsetDateTime odt = OffsetDateTime.parse(raw, OFFSET_FORMATTER);
            return odt.toLocalDateTime();
        } catch (DateTimeParseException ignored) {
        }

        // Try without offset
        try {
            return LocalDateTime.parse(raw, LOCAL_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        log.error("Invalid OCPI timestamp format: " + raw);
        throw new IOException("Invalid OCPI timestamp format: " + raw);
    }

}
