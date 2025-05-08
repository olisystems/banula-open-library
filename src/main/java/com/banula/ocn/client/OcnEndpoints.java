package com.banula.ocn.client;

public enum OcnEndpoints {
    // Registration
    GENERATE_REGISTRATION_TOKEN("/admin/generate-registration-token"),
    REGISTER_PARTY_CREDENTIALS("/ocpi/2.2/credentials"),
    // Commands
    START_SESSION("/ocpi/receiver/2.2/commands/START_SESSION"),
    STOP_SESSION("/ocpi/receiver/2.2/commands/STOP_SESSION"),
    RESERVE_NOW("/ocpi/receiver/2.2/commands/RESERVE_NOW"),
    CANCEL_RESEVATION("/ocpi/receiver/2.2/commands/CANCEL_RESERVATION"),
    UNLOCK_CONNECTOR("/ocpi/receiver/2.2/commands/UNLOCK_CONNECTOR"),

    // CDRs
    POST_CLIENT_OWNED_CDR("/ocpi/receiver/2.2/cdrs"),

    // Tariffs
    TARIFFS("/ocpi/receiver/2.2/tariffs"),
    TARIFFS_SENDER("/ocpi/sender/2.2/tariffs"),
    SESSION("/ocpi/receiver/2.2/sessions"),
    SESSION_SENDER("/ocpi/sender/2.2/sessions"),

    // tokens
    TOKENS("/ocpi/receiver/2.2/tokens"),
    TOKENS_SENDER("/ocpi/sender/2.2/tokens"),
    // Locations
    LOCATIONS("/ocpi/receiver/2.2/locations"),
    LOCATIONS_SENDER("/ocpi/sender/2.2/locations"),

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
