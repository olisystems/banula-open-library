package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.dto.TokenDTO;
import com.banula.openlib.ocpi.model.vo.CdrToken;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

@Mapper(componentModel = "spring")
public interface TokenMapper {

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
