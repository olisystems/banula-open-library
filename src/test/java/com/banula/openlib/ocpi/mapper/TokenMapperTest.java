package com.banula.openlib.ocpi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.model.enums.TokenType;
import com.banula.openlib.ocpi.model.enums.WhitelistType;
import com.banula.openlib.ocpi.model.vo.CdrToken;
import com.banula.openlib.ocpi.model.vo.EnergyContract;

public class TokenMapperTest {

    @Test
    void testFromTokenToCdrToken() {
        Token token = createSampleToken();
        CdrToken cdrToken = TokenMapper.fromTokenToCdrToken(token);
        assertEquals(token.getUid(), cdrToken.getUid());
        assertEquals(token.getType(), cdrToken.getType());
        assertEquals(token.getContractId(), cdrToken.getContractId());
        assertEquals(token.getCountryCode(), cdrToken.getCountryCode());
        assertEquals(token.getPartyId(), cdrToken.getPartyId());
    }

    private Token createSampleToken() {
        Token token = new Token();
        token.setUid("sampleUid");
        token.setGroupId("sampleGroupId");
        token.setValid(true);
        token.setIssuer("sampleIssuer");
        token.setType(TokenType.APP_USER);
        token.setPartyId("samplePartyId");
        token.setCountryCode("sampleCountryCode");
        token.setEnergyContract(new EnergyContract());
        token.setVisualNumber("sampleVisualNumber");
        token.setWhitelist(WhitelistType.ALWAYS);
        token.setContractId("sampleContractId");
        token.setDefaultProfileType(ProfileType.CHEAP);
        token.setLanguage("sampleLanguage");
        token.setLastUpdated(LocalDateTime.now());
        return token;
    }
}
