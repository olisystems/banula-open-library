package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.CommandResultType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CommandResult {

    /**
     * Result of the command request as sent by the Charge Point to the CPO.
     */
    @NotNull(message = "Result must not be null")
    @JsonProperty("result")
    private CommandResultType result;

    /**
     * Human-readable description of the reason (if one can be provided), multiple
     * languages can be provided.
     */
    @Valid
    @JsonProperty("message")
    private DisplayText message;

}
