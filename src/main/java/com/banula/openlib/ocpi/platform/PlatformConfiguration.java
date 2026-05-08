package com.banula.openlib.ocpi.platform;

import java.util.HashMap;

import com.banula.openlib.ocpi.model.VersionDetails;

public interface PlatformConfiguration {
    String getPlatformUrl();

    HashMap<String, VersionDetails> getOcnVersionDetails();

    void setOcnVersionDetails(String tenantId, VersionDetails versionDetails);

    boolean isToLogCurlCommands();

}
