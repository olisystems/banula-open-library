package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandResultResponse {
    @JsonProperty("uid")
    @NotNull(message = "UID cannot be null")
    @Size(max = 36, message = "UID cannot be longer than 36 characters")
    @NonNull
    private String uid;

    @JsonProperty("command_result")
    private CommandResult commandResult;

    @JsonProperty("request")
    private Object request;

}
