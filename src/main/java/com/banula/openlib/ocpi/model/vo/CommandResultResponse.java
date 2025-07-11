package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
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
