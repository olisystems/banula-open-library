package com.banula.ocpi.mapper;

import com.banula.ocpi.model.CpoToken;
import com.banula.ocpi.model.EmspToken;
import com.banula.ocpi.model.Token;
import com.banula.ocpi.model.dto.TokenDTO;
import com.banula.ocpi.model.vo.CdrToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    static CpoToken toCpoTokenEntity(TokenDTO tokenDTO){
        return CpoToken.builder()
                .uid(tokenDTO.getUid())
                .groupId(tokenDTO.getGroupId())
                .valid(tokenDTO.getValid())
                .issuer(tokenDTO.getIssuer())
                .type(tokenDTO.getType())
                .partyId(tokenDTO.getPartyId())
                .countryCode(tokenDTO.getCountryCode())
                .energyContract(tokenDTO.getEnergyContract())
                .visualNumber(tokenDTO.getVisualNumber())
                .whitelist(tokenDTO.getWhitelist())
                .contractId(tokenDTO.getContractId())
                .defaultProfileType(tokenDTO.getDefaultProfileType())
                .language(tokenDTO.getLanguage())
                .lastUpdated(tokenDTO.getLastUpdated())
                .build();
    }

    static EmspToken toEmspTokenEntity(TokenDTO tokenDTO) {
        if(tokenDTO == null) return null;
        return EmspToken.builder()
                .uid(tokenDTO.getUid())
                .groupId(tokenDTO.getGroupId())
                .valid(tokenDTO.getValid())
                .issuer(tokenDTO.getIssuer())
                .type(tokenDTO.getType())
                .partyId(tokenDTO.getPartyId())
                .countryCode(tokenDTO.getCountryCode())
                .energyContract(tokenDTO.getEnergyContract())
                .visualNumber(tokenDTO.getVisualNumber())
                .whitelist(tokenDTO.getWhitelist())
                .contractId(tokenDTO.getContractId())
                .defaultProfileType(tokenDTO.getDefaultProfileType())
                .language(tokenDTO.getLanguage())
                .lastUpdated(tokenDTO.getLastUpdated())
                .build();
    }

    static TokenDTO toTokenDTO(EmspToken chargingToken){
        return TokenDTO.builder()
                .uid(chargingToken.getUid())
                .groupId(chargingToken.getGroupId())
                .valid(chargingToken.getValid())
                .issuer(chargingToken.getIssuer())
                .type(chargingToken.getType())
                .partyId(chargingToken.getPartyId())
                .countryCode(chargingToken.getCountryCode())
                .energyContract(chargingToken.getEnergyContract())
                .visualNumber(chargingToken.getVisualNumber())
                .whitelist(chargingToken.getWhitelist())
                .contractId(chargingToken.getContractId())
                .defaultProfileType(chargingToken.getDefaultProfileType())
                .language(chargingToken.getLanguage())
                .lastUpdated(chargingToken.getLastUpdated())
                .build();
    }

    static TokenDTO toTokenDTO(CpoToken chargingToken){
        return TokenDTO.builder()
                .uid(chargingToken.getUid())
                .groupId(chargingToken.getGroupId())
                .valid(chargingToken.getValid())
                .issuer(chargingToken.getIssuer())
                .type(chargingToken.getType())
                .partyId(chargingToken.getPartyId())
                .countryCode(chargingToken.getCountryCode())
                .energyContract(chargingToken.getEnergyContract())
                .visualNumber(chargingToken.getVisualNumber())
                .whitelist(chargingToken.getWhitelist())
                .contractId(chargingToken.getContractId())
                .defaultProfileType(chargingToken.getDefaultProfileType())
                .language(chargingToken.getLanguage())
                .lastUpdated(chargingToken.getLastUpdated())
                .build();
    }

    static CdrToken fromCpoTokenToCdrToken (CpoToken cpoToken) {
        return CdrToken.builder()
                .uid(cpoToken.getUid())
                .type(cpoToken.getType())
                .partyId(cpoToken.getPartyId())
                .countryCode(cpoToken.getCountryCode())
                .contractId(cpoToken.getContractId())
                .build();
    }

    static CdrToken fromEmspTokenToCdrToken (EmspToken emspToken) {
        return CdrToken.builder()
                .uid(emspToken.getUid())
                .type(emspToken.getType())
                .partyId(emspToken.getPartyId())
                .countryCode(emspToken.getCountryCode())
                .contractId(emspToken.getContractId())
                .build();
    }

    static Token toTokenEntity(TokenDTO tokenDTO){
        return Token.builder()
                .uid(tokenDTO.getUid())
                .groupId(tokenDTO.getGroupId())
                .valid(tokenDTO.getValid())
                .issuer(tokenDTO.getIssuer())
                .type(tokenDTO.getType())
                .partyId(tokenDTO.getPartyId())
                .countryCode(tokenDTO.getCountryCode())
                .energyContract(tokenDTO.getEnergyContract())
                .visualNumber(tokenDTO.getVisualNumber())
                .whitelist(tokenDTO.getWhitelist())
                .contractId(tokenDTO.getContractId())
                .defaultProfileType(tokenDTO.getDefaultProfileType())
                .language(tokenDTO.getLanguage())
                .lastUpdated(tokenDTO.getLastUpdated())
                .build();
    }

    static CdrToken fromTokenToCdrToken (Token token) {
        return CdrToken.builder()
                .uid(token.getUid())
                .type(token.getType())
                .partyId(token.getPartyId())
                .countryCode(token.getCountryCode())
                .contractId(token.getContractId())
                .build();
    }

}
