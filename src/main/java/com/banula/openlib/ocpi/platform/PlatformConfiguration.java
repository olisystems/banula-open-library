package com.banula.openlib.ocpi.platform;

import java.util.HashMap;

import com.banula.openlib.ocn.model.OcnVersionDetails;

public interface PlatformConfiguration {
    String getPlatformUrl();

    HashMap<String, OcnVersionDetails> getOcnVersionDetails();

    void setOcnVersionDetails(String tenantId, OcnVersionDetails ocnVersionDetails);

}
