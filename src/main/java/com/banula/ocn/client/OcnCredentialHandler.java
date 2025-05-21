package com.banula.ocn.client;

import com.banula.ocn.model.OcnCredential;

public interface OcnCredentialHandler {
    OcnCredential getOcnCredential();

    void saveOcnCredential(OcnCredential ocnCredential);

    void deleteOcnCredential();
}
