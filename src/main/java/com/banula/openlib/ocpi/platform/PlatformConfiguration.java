package com.banula.openlib.ocpi.platform;

import com.banula.openlib.ocn.model.OcnVersionDetails;

public interface PlatformConfiguration {
    String getPlatformUrl();

    OcnVersionDetails getOcnVersionDetails();

    void setOcnVersionDetails(OcnVersionDetails ocnVersionDetails);

}
