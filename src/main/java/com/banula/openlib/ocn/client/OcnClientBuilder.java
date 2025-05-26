package com.banula.openlib.ocn.client;

import java.util.List;

import com.banula.openlib.ocn.model.OcnClientConfiguration;
import com.banula.openlib.ocpi.model.enums.Role;

public class OcnClientBuilder {
    OcnClientConfiguration configuration;
    OcnClient ocnClient;
    OcnCredentialHandler ocnCredentialHandler;
    OcnVersionDetailsHandler ocnVersionDetailsHandler;

    public OcnClientBuilder() {
        this.configuration = new OcnClientConfiguration();
    }

    public OcnClientBuilder setNodeUrl(String nodeUrl) {
        this.configuration.setNodeUrl(nodeUrl);
        return this;
    }

    public OcnClientBuilder setFrom(String countryCode, String partyId) {
        this.configuration.setFromCountryCode(countryCode);
        this.configuration.setFromPartyId(partyId);
        return this;
    }

    public OcnClientBuilder setTo(String countryCode, String partyId) {
        this.configuration.setToCountryCode(countryCode);
        this.configuration.setToPartyId(partyId);
        return this;
    }

    public OcnClientBuilder setTokenB(String token) {
        this.configuration.setTokenB(token);
        return this;
    }

    public OcnClientBuilder setPrivateKey(String privateKey) {
        this.configuration.setPrivateKey(privateKey);
        return this;
    }

    public OcnClientBuilder setSupportsSigning(boolean supports) {
        this.configuration.setSigningSupported(supports);
        return this;
    }

    public OcnClientBuilder setAdminKey(String adminKey) {
        this.configuration.setAdminKey(adminKey);
        return this;
    }

    public OcnClientBuilder setUpdatePartyOnStart(boolean updateOnStart) {
        this.configuration.setUpdatingParty(updateOnStart);
        return this;
    }

    public OcnClientBuilder setOcpiRoles(List<Role> roles) {
        this.configuration.setOcpiRoles(roles);
        return this;
    }

    public OcnClientBuilder setPartyBackendUrl(String url) {
        this.configuration.setPartyBackendUrl(url);
        return this;
    }

    public OcnClientBuilder setOcnCredentialHandler(OcnCredentialHandler ocnCredentialHandler) {
        this.ocnCredentialHandler = ocnCredentialHandler;
        return this;
    }

    public OcnClientBuilder setOcnVersionDetailsHandler(OcnVersionDetailsHandler ocnVersionDetailsHandler) {
        this.ocnVersionDetailsHandler = ocnVersionDetailsHandler;
        return this;
    }

    public OcnClientBuilder setIntegrationTestParameter(String integrationTest) {
        this.configuration.setIntegrationTestParameter(integrationTest);
        return this;
    }

    public OcnClient build() {
        return new OcnClient(this.configuration, ocnCredentialHandler, ocnVersionDetailsHandler);
    }
}
