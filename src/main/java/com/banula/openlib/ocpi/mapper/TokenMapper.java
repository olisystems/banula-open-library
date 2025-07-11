package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.CpoToken;
import com.banula.openlib.ocpi.model.EmspToken;
import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.dto.TokenDTO;
import com.banula.openlib.ocpi.model.vo.CdrToken;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    static CpoToken toCpoTokenEntity(TokenDTO tokenDTO) {
        if (tokenDTO == null) {
            return null;
        }
        CpoToken cpoToken = new CpoToken();
        BeanUtils.copyProperties(tokenDTO, cpoToken);
        return cpoToken;
    }

    static EmspToken toEmspTokenEntity(TokenDTO tokenDTO) {
        if (tokenDTO == null) {
            return null;
        }
        EmspToken emspToken = new EmspToken();
        BeanUtils.copyProperties(tokenDTO, emspToken);
        return emspToken;
    }

    static TokenDTO toTokenDTO(EmspToken chargingToken) {
        if (chargingToken == null) {
            return null;
        }
        TokenDTO tokenDTO = new TokenDTO();
        BeanUtils.copyProperties(chargingToken, tokenDTO);
        return tokenDTO;
    }

    static TokenDTO toTokenDTO(CpoToken chargingToken) {
        if (chargingToken == null) {
            return null;
        }
        TokenDTO tokenDTO = new TokenDTO();
        BeanUtils.copyProperties(chargingToken, tokenDTO);
        return tokenDTO;
    }

    static CdrToken fromCpoTokenToCdrToken(CpoToken cpoToken) {
        return CdrToken.builder()
                .uid(cpoToken.getUid())
                .type(cpoToken.getType())
                .partyId(cpoToken.getPartyId())
                .countryCode(cpoToken.getCountryCode())
                .contractId(cpoToken.getContractId())
                .build();
    }

    static CdrToken fromEmspTokenToCdrToken(EmspToken emspToken) {
        return CdrToken.builder()
                .uid(emspToken.getUid())
                .type(emspToken.getType())
                .partyId(emspToken.getPartyId())
                .countryCode(emspToken.getCountryCode())
                .contractId(emspToken.getContractId())
                .build();
    }

    static Token toTokenEntity(TokenDTO tokenDTO) {
        if (tokenDTO == null) {
            return null;
        }
        Token token = new Token();
        BeanUtils.copyProperties(tokenDTO, token);
        return token;
    }

    static CdrToken fromTokenToCdrToken(Token token) {
        return CdrToken.builder()
                .uid(token.getUid())
                .type(token.getType())
                .partyId(token.getPartyId())
                .countryCode(token.getCountryCode())
                .contractId(token.getContractId())
                .build();
    }

}
