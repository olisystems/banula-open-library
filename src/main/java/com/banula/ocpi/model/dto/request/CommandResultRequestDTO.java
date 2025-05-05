package com.banula.ocpi.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.CommandResultType;
import com.banula.ocpi.model.vo.DisplayText;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandResultRequestDTO {
    @NotNull
    @JsonProperty("result")
    private CommandResultType result;
    @JsonProperty("message")
    private DisplayText message;
}
