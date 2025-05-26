package com.banula.openlib.mapper;

import com.banula.openlib.ocpi.mapper.CommandResultMapper;
import com.banula.openlib.ocpi.model.dto.request.CommandResultRequestDTO;
import com.banula.openlib.ocpi.model.enums.CommandResultType;
import com.banula.openlib.ocpi.model.vo.CommandResult;
import com.banula.openlib.ocpi.model.vo.DisplayText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandResultMapperTest {

    private CommandResult commandResult;
    private CommandResultRequestDTO commandResultRequestDTO;

    @BeforeEach
    void setUp() {
        // Initialize CommandResult object
        commandResult = new CommandResult();
        commandResult.setResult(CommandResultType.ACCEPTED);
        commandResult.setMessage(new DisplayText("en", "Command accepted"));

        // Initialize CommandResultRequestDTO object
        commandResultRequestDTO = new CommandResultRequestDTO();
        commandResultRequestDTO.setResult(CommandResultType.ACCEPTED);
        commandResultRequestDTO.setMessage(new DisplayText("en", "Command accepted"));
    }

    @Test
    void testToCommandResultRequestDTO() {
        CommandResultRequestDTO mappedDTO = CommandResultMapper.toCommandResultRequestDTO(commandResult);
        assertNotNull(mappedDTO);
        assertEquals(commandResult.getResult(), mappedDTO.getResult());
        assertEquals(commandResult.getMessage().getLanguage(), mappedDTO.getMessage().getLanguage());
        assertEquals(commandResult.getMessage().getText(), mappedDTO.getMessage().getText());
    }

    @Test
    void testToCommandResultEntity() {
        CommandResult mappedEntity = CommandResultMapper.toCommandResultEntity(commandResultRequestDTO);
        assertNotNull(mappedEntity);
        assertEquals(commandResultRequestDTO.getResult(), mappedEntity.getResult());
        assertEquals(commandResultRequestDTO.getMessage().getLanguage(), mappedEntity.getMessage().getLanguage());
        assertEquals(commandResultRequestDTO.getMessage().getText(), mappedEntity.getMessage().getText());
    }

    @Test
    void testToCommandResultRequestDTO_Null() {
        CommandResultRequestDTO mappedDTO = CommandResultMapper.toCommandResultRequestDTO(null);
        assertNull(mappedDTO);
    }

    @Test
    void testToCommandResultEntity_Null() {
        CommandResult mappedEntity = CommandResultMapper.toCommandResultEntity(null);
        assertNull(mappedEntity);
    }

    // New Tests for List Conversion

    @Test
    void testToListCommandResultDTO() {
        List<CommandResult> commandResultList = Collections.singletonList(commandResult);
        List<CommandResultRequestDTO> commandResultDTOList = CommandResultMapper
                .toListCommandResultDTO(commandResultList);
        assertNotNull(commandResultDTOList);
        assertEquals(1, commandResultDTOList.size());
        assertEquals(commandResult.getResult(), commandResultDTOList.get(0).getResult());
        assertEquals(commandResult.getMessage().getLanguage(), commandResultDTOList.get(0).getMessage().getLanguage());
    }

    @Test
    void testToListCommandResultEntity() {
        List<CommandResultRequestDTO> commandResultDTOList = Collections.singletonList(commandResultRequestDTO);
        List<CommandResult> commandResultList = CommandResultMapper.toListCommandResultEntity(commandResultDTOList);
        assertNotNull(commandResultList);
        assertEquals(1, commandResultList.size());
        assertEquals(commandResultRequestDTO.getResult(), commandResultList.get(0).getResult());
        assertEquals(commandResultRequestDTO.getMessage().getLanguage(),
                commandResultList.get(0).getMessage().getLanguage());
    }

    @Test
    void testToListCommandResultDTO_Null() {
        List<CommandResultRequestDTO> commandResultDTOList = CommandResultMapper.toListCommandResultDTO(null);
        assertNull(commandResultDTOList);
    }

    @Test
    void testToListCommandResultEntity_Null() {
        List<CommandResult> commandResultList = CommandResultMapper.toListCommandResultEntity(null);
        assertNull(commandResultList);
    }
}
