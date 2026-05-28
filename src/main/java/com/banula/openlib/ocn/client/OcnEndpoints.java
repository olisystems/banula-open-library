package com.banula.openlib.ocn.client;

public enum OcnEndpoints {
    // Admin endpoints
    CREATE_PLATFORM("/admin/platform"),
    DELETE_PARTY_CREDENTIALS("/admin/party"),

    // OCPI MAIN ENDPOINTS
    VERSIONS("/ocpi/versions"),

    // OCPI CUSTOM MODULES
    SCSP_CALCULATE_FLEXIBILITY("/ocpi/custom/receiver/flexibility"),
    CUSTOM_SMART_LOCATIONS("ocpi/custom/smartlocations");

    private String mainUrl;

    private OcnEndpoints(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    @Override
    public String toString() {
        return this.mainUrl;
    }
}
