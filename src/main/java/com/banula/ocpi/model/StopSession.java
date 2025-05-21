package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopSession {

    /**
     * Session.id of the Session that is requested to be stopped.
     */
    @JsonProperty("session_id")
    @NotEmpty(message = "sessionId cannot be empty.")
    @Size(min=1, max = 36, message = "Session id longer than 36 characters")
    private String sessionId;

    /**
     * Response URL for the command.
     */
    @JsonProperty("response_url")
    @NotEmpty(message = "responseUrl cannot be empty.")
    private String responseUrl;

}
