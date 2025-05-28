package com.banula.openlib.ocn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValuesToSign<T> {
    public SignableHeaders headers;

    public Map<String, ?> params;

    public T body;

    public ValuesToSign(ValuesToSign<T> obj) {
        this.headers = obj.headers;
        this.params = obj.params;
        this.body = obj.body;
    }

    public ValuesToSign(Map<String, String> headers, T body, Map<String, String> params) {
        SignableHeaders signableHeaders = new SignableHeaders();

        if (headers.containsKey("X-Correlation-ID"))
            signableHeaders.setCorrelationId(headers.get("X-Correlation-ID"));
        if (headers.containsKey("OCPI-from-country-code"))
            signableHeaders.setFromCountryCode(headers.get("OCPI-from-country-code"));
        if (headers.containsKey("OCPI-from-party-id"))
            signableHeaders.setFromPartyId(headers.get("OCPI-from-party-id"));
        if (headers.containsKey("X-Limit"))
            signableHeaders.setLimit(headers.get("X-Limit"));
        if (headers.containsKey("Link"))
            signableHeaders.setLink(headers.get("Link"));
        if (headers.containsKey("Location"))
            signableHeaders.setLocation(headers.get("Location"));
        if (headers.containsKey("OCPI-to-country-code"))
            signableHeaders.setToCountryCode(headers.get("OCPI-to-country-code"));
        if (headers.containsKey("OCPI-to-party-id"))
            signableHeaders.setToPartyId(headers.get("OCPI-to-party-id"));
        if (headers.containsKey("X-Total-Count"))
            signableHeaders.setTotalCount(headers.get("X-Total-Count"));

        this.setHeaders(signableHeaders);
        this.setBody(body);
        this.setParams(params);
    }
}