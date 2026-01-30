package com.banula.openlib.mapper;

import com.banula.openlib.ocpi.mapper.TokenMapper;
import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.dto.TokenDTO;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.model.enums.TokenType;
import com.banula.openlib.ocpi.model.enums.WhitelistType;
import com.banula.openlib.ocpi.model.vo.EnergyContract;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenMapperTest {

    @Test
    void testToTokenEntity() {
        TokenDTO tokenDTO = createSampleTokenDTO();
        Token token = TokenMapper.toTokenEntity(tokenDTO);
        assertEquals(tokenDTO.getUid(), token.getUid());
        assertEquals(tokenDTO.getGroupId(), token.getGroupId());
        assertEquals(tokenDTO.getValid(), token.getValid());
        assertEquals(tokenDTO.getIssuer(), token.getIssuer());
        assertEquals(tokenDTO.getType(), token.getType());
        assertEquals(tokenDTO.getPartyId(), token.getPartyId());
        assertEquals(tokenDTO.getCountryCode(), token.getCountryCode());
        assertEquals(tokenDTO.getEnergyContract(), token.getEnergyContract());
        assertEquals(tokenDTO.getVisualNumber(), token.getVisualNumber());
        assertEquals(tokenDTO.getWhitelist(), token.getWhitelist());
        assertEquals(tokenDTO.getContractId(), token.getContractId());
        assertEquals(tokenDTO.getDefaultProfileType(), token.getDefaultProfileType());
        assertEquals(tokenDTO.getLanguage(), token.getLanguage());
        assertEquals(tokenDTO.getLastUpdated(), token.getLastUpdated());
    }

    @Test
    void testToTokenDTO() {
        Token token = createSampleToken();
        TokenDTO tokenDTO = TokenMapper.toTokenDTO(token);
        assertEquals(token.getUid(), tokenDTO.getUid());
        assertEquals(token.getGroupId(), tokenDTO.getGroupId());
        assertEquals(token.getValid(), tokenDTO.getValid());
        assertEquals(token.getIssuer(), tokenDTO.getIssuer());
        assertEquals(token.getType(), tokenDTO.getType());
        assertEquals(token.getPartyId(), tokenDTO.getPartyId());
        assertEquals(token.getCountryCode(), tokenDTO.getCountryCode());
        assertEquals(token.getEnergyContract(), tokenDTO.getEnergyContract());
        assertEquals(token.getVisualNumber(), tokenDTO.getVisualNumber());
        assertEquals(token.getWhitelist(), tokenDTO.getWhitelist());
        assertEquals(token.getContractId(), tokenDTO.getContractId());
        assertEquals(token.getDefaultProfileType(), tokenDTO.getDefaultProfileType());
        assertEquals(token.getLanguage(), tokenDTO.getLanguage());
        assertEquals(token.getLastUpdated(), tokenDTO.getLastUpdated());
    }

    private TokenDTO createSampleTokenDTO() {
        return TokenDTO.builder()
                .uid("sampleUid")
                .groupId("sampleGroupId")
                .valid(true)
                .issuer("sampleIssuer")
                .type(TokenType.APP_USER)
                .partyId("samplePartyId")
                .countryCode("sampleCountryCode")
                .energyContract(new EnergyContract())
                .visualNumber("sampleVisualNumber")
                .whitelist(WhitelistType.ALWAYS)
                .contractId("sampleContractId")
                .defaultProfileType(ProfileType.CHEAP)
                .language("sampleLanguage")
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    private Token createSampleToken() {
        return Token.builder()
                .uid("sampleUid")
                .groupId("sampleGroupId")
                .valid(true)
                .issuer("sampleIssuer")
                .type(TokenType.APP_USER)
                .partyId("samplePartyId")
                .countryCode("sampleCountryCode")
                .energyContract(new EnergyContract())
                .visualNumber("sampleVisualNumber")
                .whitelist(WhitelistType.ALWAYS)
                .contractId("sampleContractId")
                .defaultProfileType(ProfileType.CHEAP)
                .language("sampleLanguage")
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}