package com.banula.ocn.client;

import com.banula.ocn.model.OcnVersionDetails;

public interface OcnVersionDetailsHandler {
    OcnVersionDetails getVersionDetails();

    void saveVersionDetails(OcnVersionDetails endpointResponse);

}
