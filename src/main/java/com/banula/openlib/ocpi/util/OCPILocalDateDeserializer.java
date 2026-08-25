package com.banula.openlib.ocpi.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deserializes a plain ISO calendar day ({@code yyyy-MM-dd}), tolerating a full
 * ISO date-time by keeping only its date part.
 */
@Slf4j
public class OCPILocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String raw = p.getText().trim();

        try {
            return LocalDate.parse(raw, DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }

        // Tolerate a full date-time payload by keeping only the calendar day.
        int separator = raw.indexOf('T');
        if (separator > 0) {
            try {
                return LocalDate.parse(raw.substring(0, separator), DATE_FORMATTER);
            } catch (DateTimeParseException ignored) {
            }
        }

        log.error("Invalid OCPI date format: " + raw);
        throw new IOException("Invalid OCPI date format: " + raw);
    }

}
