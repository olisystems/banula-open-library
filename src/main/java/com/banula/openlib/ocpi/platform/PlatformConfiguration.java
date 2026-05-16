package com.banula.openlib.ocpi.platform;

import com.banula.openlib.ocpi.model.enums.Role;
import com.banula.openlib.ocpi.model.enums.VersionNumber;

public interface PlatformConfiguration {
    String getPlatformUrl();

    Role getOcpiRole();

    VersionNumber getOcpiVersion();

    boolean isToLogCurlCommands();

}
