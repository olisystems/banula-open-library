package com.banula.openlib.ocn.client;

import com.banula.openlib.ocpi.model.VersionDetails;

public interface OcnVersionDetailsHandler {
    VersionDetails getVersionDetails();

    void saveVersionDetails(VersionDetails endpointResponse);

}
