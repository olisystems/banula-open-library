package com.banula.openlib.ocn.client;

import com.banula.openlib.ocn.model.OcnVersionDetails;

public interface OcnVersionDetailsHandler {
    OcnVersionDetails getVersionDetails();

    void saveVersionDetails(OcnVersionDetails endpointResponse);

}
