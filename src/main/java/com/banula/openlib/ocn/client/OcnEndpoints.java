package com.banula.openlib.ocn.client;

public enum OcnEndpoints {
    // Registration
    GENERATE_REGISTRATION_TOKEN("/admin/generate-registration-token"),
    REGISTER_PARTY_CREDENTIALS("/ocpi/2.2/credentials"),

    // CUSTOM MODULES
    // SCSP
    SCSP_CALCULATE_FLEXIBILITY("/ocpi/custom/receiver/flexibility"),
    CUSTOM_SMART_LOCATIONS("ocpi/custom/smartlocations");

    private String url;

    private OcnEndpoints(String endpointUrl) {
        this.url = endpointUrl;
    }

    @Override
    public String toString() {
        return this.url;
    }
}
