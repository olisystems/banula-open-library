package com.banula.ocn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.banula.ocn.model.RewriteVerifyResult;
import com.banula.ocn.model.ValuesToSign;
import lombok.Getter;
import org.web3j.crypto.Hash;

import java.util.List;
import java.util.Map;

public class Rewrite {

    private final Map<String, Object> rewrittenFields;
    @Getter
    private final String hash;
    @Getter
    private final String rsv;
    @Getter
    private final String signatory;

    public Rewrite(Map<String, Object> rewrittenFields, String hash, String rsv, String signatory) {
        this.rewrittenFields = rewrittenFields;

        this.hash = hash;
        this.rsv = rsv;
        this.signatory = signatory;
    }

    public RewriteVerifyResult verify(List<String> fields, ValuesToSign<?> modifiedValues, DocumentContext jsonPath, ObjectMapper objectMapper) throws Exception {
        String valuesAsJsonString = objectMapper.writeValueAsString(modifiedValues);

        // 1. Rebuild previous ValuesToSign using rewritten fields map
        for (Map.Entry<String, Object> entry : rewrittenFields.entrySet()) {
            jsonPath.set(entry.getKey(), entry.getValue());
        }

        // 2. Verify hash matches stashed hash
        StringBuilder message = new StringBuilder();
        for (String field : fields) {
            Object fieldValue = jsonPath.read(field.toLowerCase());
            if (fieldValue != null) {
                message.append(fieldValue);
            }
        }

        if (!hash.equals(Hash.sha3String(message.toString()))) {
            return new RewriteVerifyResult(false, "Stashed values do not match original.");
        }

        // 3. Verify signatory matches stashed signatory
        String expectedSignatory = Helpers.signerOfMessage(hash, rsv);
        if (!Helpers.toChecksumAddress(signatory).equals(Helpers.toChecksumAddress(expectedSignatory))) {
            return new RewriteVerifyResult(false, "Signatory of stashed rewrite is incorrect.");
        }

        ValuesToSign<?> originalValues = objectMapper.readValue(jsonPath.jsonString(), modifiedValues.getClass());
        return new RewriteVerifyResult(true, null, originalValues);
    }
}