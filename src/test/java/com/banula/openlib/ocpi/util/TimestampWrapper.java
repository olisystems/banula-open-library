package com.banula.openlib.ocpi.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimestampWrapper {
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
}
