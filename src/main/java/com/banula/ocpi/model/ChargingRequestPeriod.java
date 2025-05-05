package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.model.vo.CdrDimension;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargingRequestPeriod implements Serializable {

    @JsonProperty("start_date_time")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @JsonProperty("dimensions")
    private List<CdrDimension> dimensions;

    @JsonProperty("sessionId")
    private String sessionId;

}
