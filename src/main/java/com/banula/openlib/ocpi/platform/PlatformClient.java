package com.banula.openlib.ocpi.platform;

import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.banula.openlib.ocn.client.GenericTypeRefUtil;
import com.banula.openlib.ocn.model.OcnVersionDetails;
import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.model.OcpiResponse;
import com.banula.openlib.ocpi.model.enums.InterfaceRole;
import com.banula.openlib.ocpi.model.enums.ModuleID;
import com.banula.openlib.ocpi.model.vo.Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PlatformClient {

    private final RestTemplate restTemplate;
    private final PlatformConfiguration platformConfiguration;

    // TODO the idea of this endpoint is to replace the OcnClient class for API
    // Roles communication with the Platform
    public <T, N> OcpiResponse<T> sendOutflowRequest(String toOcpiCountryCode, String toOcpiPartyId,
            InterfaceRole interfaceRole, ModuleID moduleID, HttpMethod method, N body, Class<T> refType) {
        log.info("Sending outflow request to platform for country code: {} and party id: {}", toOcpiCountryCode,
                toOcpiPartyId);
        try {
            HttpHeaders headers = createHeaders(toOcpiCountryCode, toOcpiPartyId);
            String platformEndpoint = getOutflowUrl(moduleID, interfaceRole);
            HttpEntity<N> entity = new HttpEntity<>(body, headers);
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
            ResponseEntity<OcpiResponse<T>> response = restTemplate.exchange(
                    platformEndpoint,
                    method,
                    entity,
                    responseTypeRef);
            return response.getBody();
        } catch (Exception ex) {
            log.error("Error while sending outflow request to platform, error message: {}", ex.getLocalizedMessage());
            throw new OCPICustomException("Error while sending outflow request to platform");
        }
    }

    public void updateOcnVersionDetailsFromPlatform() {
        try {
            log.info("Updating OCN version details from platform | Platform URL: {}",
                    platformConfiguration.getPlatformUrl());
            HttpHeaders headers = createHeaders();
            String platformEndpoint = getOcnVersionDetailsUrl();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<OcpiResponse<OcnVersionDetails>> response = restTemplate.exchange(
                    platformEndpoint,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<OcpiResponse<OcnVersionDetails>>() {
                    });
            OcpiResponse<OcnVersionDetails> responseBody = response.getBody();
            if (responseBody == null) {
                log.error("Received null response body from platform trying to retrieve Ocn Endpoints");
                return;
            }
            platformConfiguration.setOcnVersionDetails(responseBody.getData());
        } catch (Exception ex) {
            log.error("Error while sending outflow request to platform, error message: {}", ex.getLocalizedMessage());
            throw new OCPICustomException("Error while sending outflow request to platform");
        }
    }

    private HttpHeaders createHeaders(String toOcpiCountryCode, String toOcpiPartyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Correlation-ID", UUID.randomUUID().toString());
        headers.set("OCPI-to-country-code", toOcpiCountryCode);
        headers.set("OCPI-to-party-id", toOcpiPartyId);
        return headers;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Correlation-ID", UUID.randomUUID().toString());
        return headers;
    }

    // get the proper ocn node endpoint retrieved from platform and build the
    // outflow url taking into account the module id and interface role
    public String getOutflowUrl(ModuleID moduleID, InterfaceRole interfaceRole) {
        if (platformConfiguration.getOcnVersionDetails() == null) {
            throw new IllegalArgumentException("OCN version details not found");
        }

        Endpoint endpoint = platformConfiguration.getOcnVersionDetails().getEndpoints().stream()
                .filter(e -> e.getIdentifier() == moduleID && e.getRole() == interfaceRole)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
        String url = endpoint.getUrl();
        int startIndex = url.indexOf("ocpi/");
        String endpointUrl = url.substring(startIndex);
        return platformConfiguration.getPlatformUrl() + "/ocpi/outflow/" + endpointUrl;

    }

    private String getOcnVersionDetailsUrl() {
        return platformConfiguration.getPlatformUrl() + "/non-ocpi/ocn-version-details";
    }

}
