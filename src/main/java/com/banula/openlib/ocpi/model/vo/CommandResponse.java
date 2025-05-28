package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.CommandResponseType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class CommandResponse {

    /**
     * Response from the CPO on the command request.
     */
    @NotNull(message = "Result must not be null")
    @JsonProperty("result")
    private CommandResponseType result;

    /**
     * Timeout for this command in seconds. When the Result is not received within
     * this timeout,
     * the eMSP can assume that the message might never be send.
     */
    @NotNull(message = "Timeout must not be null")
    @JsonProperty("timeout")
    private Integer timeout;

    /**
     * Human-readable description of the result (if one can be provided), multiple
     * languages can be provided.
     */
    @Size(min = 1, message = "Message must not be empty")
    @JsonProperty("message")
    private List<DisplayText> message;

}
