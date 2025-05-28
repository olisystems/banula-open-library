package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.dto.request.CommandResultRequestDTO;
import com.banula.openlib.ocpi.model.vo.CommandResult;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommandResultMapper {

    public static CommandResultRequestDTO toCommandResultRequestDTO(CommandResult commandResult) {
        if (commandResult == null)
            return null;
        CommandResultRequestDTO dto = new CommandResultRequestDTO();
        dto.setResult(commandResult.getResult());
        dto.setMessage(commandResult.getMessage());
        return dto;
    }

    public static CommandResult toCommandResultEntity(CommandResultRequestDTO commandResultRequestDTO) {
        if (commandResultRequestDTO == null)
            return null;
        CommandResult vo = new CommandResult();
        vo.setResult(commandResultRequestDTO.getResult());
        vo.setMessage(commandResultRequestDTO.getMessage());
        return vo;
    }

    static List<CommandResultRequestDTO> toListCommandResultDTO(List<CommandResult> commandResults) {
        return commandResults == null ? null
                : commandResults.stream()
                        .map(CommandResultMapper::toCommandResultRequestDTO)
                        .collect(Collectors.toList());
    }

    static List<CommandResult> toListCommandResultEntity(List<CommandResultRequestDTO> commandResultRequestDTOs) {
        return commandResultRequestDTOs == null ? null
                : commandResultRequestDTOs.stream()
                        .map(CommandResultMapper::toCommandResultEntity)
                        .collect(Collectors.toList());
    }
}
