package com.banula.openlib.ocpi.model.dto.response;

import com.banula.openlib.ocpi.model.enums.CommandResponseType;
import com.banula.openlib.ocpi.model.vo.DisplayText;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandResponseDTO {
    @NotNull
    private CommandResponseType result;
    @NotNull
    private Integer timeout;
    private List<DisplayText> message;
}
