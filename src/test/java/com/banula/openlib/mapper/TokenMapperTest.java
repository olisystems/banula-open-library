package com.banula.openlib.mapper;

import com.banula.openlib.ocpi.mapper.TokenMapper;
import com.banula.openlib.ocpi.model.CpoToken;
import com.banula.openlib.ocpi.model.EmspToken;
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
        CpoToken chargingToken = TokenMapper.toCpoTokenEntity(tokenDTO);
        assertEquals(tokenDTO.getUid(), chargingToken.getUid());
        assertEquals(tokenDTO.getGroupId(), chargingToken.getGroupId());
        assertEquals(tokenDTO.getValid(), chargingToken.getValid());
        assertEquals(tokenDTO.getIssuer(), chargingToken.getIssuer());
        assertEquals(tokenDTO.getType(), chargingToken.getType());
        assertEquals(tokenDTO.getPartyId(), chargingToken.getPartyId());
        assertEquals(tokenDTO.getCountryCode(), chargingToken.getCountryCode());
        assertEquals(tokenDTO.getEnergyContract(), chargingToken.getEnergyContract());
        assertEquals(tokenDTO.getVisualNumber(), chargingToken.getVisualNumber());
        assertEquals(tokenDTO.getWhitelist(), chargingToken.getWhitelist());
        assertEquals(tokenDTO.getContractId(), chargingToken.getContractId());
        assertEquals(tokenDTO.getDefaultProfileType(), chargingToken.getDefaultProfileType());
        assertEquals(tokenDTO.getLanguage(), chargingToken.getLanguage());
        assertEquals(tokenDTO.getLastUpdated(), chargingToken.getLastUpdated());
    }

    @Test
    void testToTokenCpoDTO() {
        CpoToken chargingToken = createSampleChargingTokenCpo();
        TokenDTO tokenDTO = TokenMapper.toTokenDTO(chargingToken);
        assertEquals(chargingToken.getUid(), tokenDTO.getUid());
        assertEquals(chargingToken.getGroupId(), tokenDTO.getGroupId());
        assertEquals(chargingToken.getValid(), tokenDTO.getValid());
        assertEquals(chargingToken.getIssuer(), tokenDTO.getIssuer());
        assertEquals(chargingToken.getType(), tokenDTO.getType());
        assertEquals(chargingToken.getPartyId(), tokenDTO.getPartyId());
        assertEquals(chargingToken.getCountryCode(), tokenDTO.getCountryCode());
        assertEquals(chargingToken.getEnergyContract(), tokenDTO.getEnergyContract());
        assertEquals(chargingToken.getVisualNumber(), tokenDTO.getVisualNumber());
        assertEquals(chargingToken.getWhitelist(), tokenDTO.getWhitelist());
        assertEquals(chargingToken.getContractId(), tokenDTO.getContractId());
        assertEquals(chargingToken.getDefaultProfileType(), tokenDTO.getDefaultProfileType());
        assertEquals(chargingToken.getLanguage(), tokenDTO.getLanguage());
        assertEquals(chargingToken.getLastUpdated(), tokenDTO.getLastUpdated());
    }

    @Test
    void testToTokenEmspDTO() {
        EmspToken chargingToken = createSampleChargingTokenEmsp();
        TokenDTO tokenDTO = TokenMapper.toTokenDTO(chargingToken);
        assertEquals(chargingToken.getUid(), tokenDTO.getUid());
        assertEquals(chargingToken.getGroupId(), tokenDTO.getGroupId());
        assertEquals(chargingToken.getValid(), tokenDTO.getValid());
        assertEquals(chargingToken.getIssuer(), tokenDTO.getIssuer());
        assertEquals(chargingToken.getType(), tokenDTO.getType());
        assertEquals(chargingToken.getPartyId(), tokenDTO.getPartyId());
        assertEquals(chargingToken.getCountryCode(), tokenDTO.getCountryCode());
        assertEquals(chargingToken.getEnergyContract(), tokenDTO.getEnergyContract());
        assertEquals(chargingToken.getVisualNumber(), tokenDTO.getVisualNumber());
        assertEquals(chargingToken.getWhitelist(), tokenDTO.getWhitelist());
        assertEquals(chargingToken.getContractId(), tokenDTO.getContractId());
        assertEquals(chargingToken.getDefaultProfileType(), tokenDTO.getDefaultProfileType());
        assertEquals(chargingToken.getLanguage(), tokenDTO.getLanguage());
        assertEquals(chargingToken.getLastUpdated(), tokenDTO.getLastUpdated());
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

    private CpoToken createSampleChargingTokenCpo() {
        return CpoToken.builder()
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

    private EmspToken createSampleChargingTokenEmsp() {
        return EmspToken.builder()
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