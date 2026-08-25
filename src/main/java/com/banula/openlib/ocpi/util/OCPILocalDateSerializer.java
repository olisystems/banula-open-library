package com.banula.openlib.ocpi.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializes a {@link LocalDate} as a plain ISO calendar day ({@code yyyy-MM-dd}).
 *
 * <p>
 * Declared explicitly on every {@link LocalDate} field (never via
 * {@code @JsonFormat}) because this library is consumed by services that
 * configure their own {@code ObjectMapper}.
 */
public class OCPILocalDateSerializer extends JsonSerializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.format(FORMATTER));
    }

}
