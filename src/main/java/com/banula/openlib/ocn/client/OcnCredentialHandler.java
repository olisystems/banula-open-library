package com.banula.openlib.ocn.client;

import com.banula.openlib.ocn.model.OcnCredential;

public interface OcnCredentialHandler {
    OcnCredential getOcnCredential();

    void saveOcnCredential(OcnCredential ocnCredential);

    void deleteOcnCredential();
}
