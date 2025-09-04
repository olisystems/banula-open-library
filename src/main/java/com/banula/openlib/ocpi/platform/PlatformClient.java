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
import org.springframework.web.client.RestClientResponseException;

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

    public <T, N> OcpiResponse<T> sendOutflowRequest(String tenantId, String toOcpiCountryCode, String toOcpiPartyId,
            InterfaceRole interfaceRole, ModuleID moduleID, HttpMethod method, N body, Class<T> refType) {
        log.info("Sending outflow request to platform for country code: {} and party id: {}", toOcpiCountryCode,
                toOcpiPartyId);
        try {
            HttpHeaders headers = createHeaders(toOcpiCountryCode, toOcpiPartyId);
            String platformEndpoint = getOutflowUrl(tenantId, moduleID, interfaceRole);
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

    public void updateOcnVersionDetailsFromPlatform(String tenantId) {
        try {
            log.info("Updating OCN version details from platform | Platform URL: {}",
                    platformConfiguration.getPlatformUrl());
            HttpHeaders headers = createHeaders();
            String platformEndpoint = getOcnVersionDetailsUrl(tenantId);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<OcpiResponse<OcnVersionDetails>> response = restTemplate.exchange(
                    platformEndpoint,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<OcpiResponse<OcnVersionDetails>>() {
                    });
            int httpStatus = response.getStatusCode().value();
            OcpiResponse<OcnVersionDetails> responseBody = response.getBody();
            if (responseBody == null) {
                String msg = String.format("Platform returned empty body for OCN version details (HTTP %d)",
                        httpStatus);
                log.error(msg);
                throw new OCPICustomException(msg);
            }
            if (responseBody.getStatus_code() != 1000) {
                String msg = String.format(
                        "Platform error retrieving OCN version details: %s (status_code %d, HTTP %d)",
                        responseBody.getStatus_message(), responseBody.getStatus_code(), httpStatus);
                log.error(msg);
                throw new OCPICustomException(msg);
            }
            OcnVersionDetails details = responseBody.getData();
            if (details == null) {
                String msg = String.format(
                        "Platform returned no data for OCN version details (status_code %d, HTTP %d)",
                        responseBody.getStatus_code(), httpStatus);
                log.error(msg);
                throw new OCPICustomException(msg);
            }
            platformConfiguration.setOcnVersionDetails(tenantId, details);
        } catch (RestClientResponseException rex) {
            String body = rex.getResponseBodyAsString();
            String msg = String.format(
                    "HTTP %d from platform while retrieving OCN version details: %s",
                    rex.getStatusCode().value(), body);
            log.error(msg);
            throw new OCPICustomException(msg);
        } catch (NullPointerException npe) {
            String msg = "Failed to store OCN version details: platform configuration storage not initialized";
            log.error(msg);
            throw new OCPICustomException(msg);
        } catch (Exception ex) {
            log.error("Error while retrieving or updating OCN version details from platform, error message: {}",
                    ex.getLocalizedMessage());
            throw new OCPICustomException("Error while retrieving or updating OCN version details from platform");
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
    public String getOutflowUrl(String tenantId, ModuleID moduleID, InterfaceRole interfaceRole) {
        if (platformConfiguration.getOcnVersionDetails() == null) {
            throw new IllegalArgumentException("OCN version details not found");
        }

        Endpoint endpoint = platformConfiguration.getOcnVersionDetails().get(tenantId).getEndpoints().stream()
                .filter(e -> e.getIdentifier() == moduleID && e.getRole() == interfaceRole)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
        String url = endpoint.getUrl();
        int startIndex = url.indexOf("ocpi/");
        String endpointUrl = url.substring(startIndex + 5); // +5 to skip "ocpi/"
        return platformConfiguration.getPlatformUrl() + "/" + tenantId + "/proxy/ocpi/outflow/" + endpointUrl;

    }

    private String getOcnVersionDetailsUrl(String tenantId) {
        return platformConfiguration.getPlatformUrl() + "/" + tenantId + "/ocpi/ocn-version-details";
    }

}
