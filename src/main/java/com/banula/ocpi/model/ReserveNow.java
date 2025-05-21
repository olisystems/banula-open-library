package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class ReserveNow {

    @NotNull(message = "Token must not be blank")
    @JsonProperty("token")
    private Token token;

    @NotNull(message = "Expiry date must not be null")
    @JsonProperty("expiry_date")
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime expiryDate;

    @NotBlank(message = "Reservation ID must not be blank")
    @Size(min = 1, max = 36, message = "Reservation ID must be between 1 and 36 characters")
    @JsonProperty("reservation_id")
    private String reservationId;

    @NotBlank(message = "Location ID must not be blank")
    @Size(min = 1, max = 36, message = "Location ID must be between 1 and 36 characters")
    @JsonProperty("location_id")
    private String locationId;

    @Size(min = 1, max = 36, message = "EVSE UID must be between 1 and 36 characters")
    @JsonProperty("evse_uid")
    private String evseUid;

    @Size(min = 1, max = 36, message = "Authorization reference must be between 1 and 36 characters")
    @JsonProperty("authorization_reference")
    private String authorizationReference;

}
