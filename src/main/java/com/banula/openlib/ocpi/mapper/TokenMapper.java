package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.dto.TokenDTO;
import com.banula.openlib.ocpi.model.vo.CdrToken;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    static Token toTokenEntity(TokenDTO tokenDTO) {
        if (tokenDTO == null) {
            return null;
        }
        Token token = new Token();
        BeanUtils.copyProperties(tokenDTO, token);
        return token;
    }

    static TokenDTO toTokenDTO(Token token) {
        if (token == null) {
            return null;
        }
        TokenDTO tokenDTO = new TokenDTO();
        BeanUtils.copyProperties(token, tokenDTO);
        return tokenDTO;
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
