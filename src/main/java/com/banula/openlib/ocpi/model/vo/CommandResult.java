package com.banula.openlib.ocpi.model.vo;

import com.banula.openlib.ocpi.model.enums.CommandResultType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
