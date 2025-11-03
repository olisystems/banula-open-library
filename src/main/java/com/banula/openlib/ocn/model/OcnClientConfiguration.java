package com.banula.openlib.ocn.model;

import java.util.List;

import com.banula.openlib.ocpi.model.enums.Role;

import lombok.Data;

@Data
public class OcnClientConfiguration {
    private boolean signingSupported = false;
    private String fromPartyId;
    private String fromCountryCode;
    private String toPartyId;
    private String toCountryCode;
    private String tokenB;
    private String tokenC;
    private String base64TokenC;
    private String privateKey;
    private String nodeUrl;
    private String adminKey;
    private List<Role> ocpiRoles;
    private String partyBackendUrl;
    private boolean updatingParty;
    private String integrationTestParameter;
    private boolean logCurlCommand;
}