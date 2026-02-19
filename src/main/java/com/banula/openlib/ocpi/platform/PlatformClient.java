package com.banula.openlib.ocpi.platform;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.banula.openlib.ocn.client.GenericTypeRefUtil;
import com.banula.openlib.ocn.model.OcnVersionDetails;
import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.model.OcpiResponse;
import com.banula.openlib.ocpi.model.enums.InterfaceRole;
import com.banula.openlib.ocpi.model.enums.ModuleID;
import com.banula.openlib.ocpi.model.vo.Endpoint;
import com.banula.openlib.ocpi.util.InfoUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PlatformClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private final RestTemplate restTemplate;
    private final PlatformConfiguration platformConfiguration;

    public <T, N> OcpiResponse<T> sendOutflowRequest(String tenantId, String toOcpiCountryCode, String toOcpiPartyId,
            InterfaceRole interfaceRole, ModuleID moduleID, HttpMethod method, N body, Class<T> refType,
            List<String> pathVariables) {
        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        return sendOutflowRequest(tenantId, toOcpiCountryCode, toOcpiPartyId, interfaceRole, moduleID, method, body,
                responseTypeRef, pathVariables, null);
    }

    public <T, N> OcpiResponse<T> sendOutflowRequest(String tenantId, String toOcpiCountryCode, String toOcpiPartyId,
            InterfaceRole interfaceRole, ModuleID moduleID, HttpMethod method, N body,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef, List<String> pathVariables,
            java.util.Map<String, String> queryParams) {
        return executeOutflowRequest(tenantId, toOcpiCountryCode, toOcpiPartyId, interfaceRole, moduleID, method, body,
                responseTypeRef, pathVariables, queryParams);
    }

    private <T, N> OcpiResponse<T> executeOutflowRequest(String tenantId, String toOcpiCountryCode,
            String toOcpiPartyId, InterfaceRole interfaceRole, ModuleID moduleID, HttpMethod method, N body,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef, List<String> pathVariables,
            java.util.Map<String, String> queryParams) {
        log.info("Sending outflow request to platform for country code: {} and party id: {}", toOcpiCountryCode,
                toOcpiPartyId);
        try {
            HttpHeaders headers = createHeaders(toOcpiCountryCode, toOcpiPartyId);
            String platformEndpoint = getOutflowUrl(tenantId, moduleID, interfaceRole);
            String finalUrl = buildUrl(platformEndpoint, pathVariables, queryParams);
            HttpEntity<N> entity = new HttpEntity<>(body, headers);
            if (platformConfiguration.isToLogCurlCommands()) {
                String requestBody = body != null ? objectMapper.writeValueAsString(body) : null;
                InfoUtils.logCurlCommand(finalUrl, method, headers, requestBody);
            }
            ResponseEntity<OcpiResponse<T>> response = restTemplate.exchange(finalUrl, method, entity, responseTypeRef);
            OcpiResponse<T> responseBody = response.getBody();
            if (responseBody != null) {
                responseBody.setHeaders(response.getHeaders());
            }
            return responseBody;
        } catch (Exception ex) {
            log.error("Error while sending outflow request to platform, error message: {}", ex.getLocalizedMessage());
            throw new OCPICustomException("Error while sending outflow request to platform");
        }
    }

    private String buildUrl(String baseUrl, List<String> pathVariables, java.util.Map<String, String> queryParams) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        if (pathVariables != null && !pathVariables.isEmpty()) {
            for (String pathVariable : pathVariables) {
                uriBuilder.pathSegment(pathVariable);
            }
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(uriBuilder::queryParam);
        }
        return uriBuilder.encode().toUriString();
    }

    public void updateOcnVersionDetailsFromPlatform(String tenantId) {
        try {
            log.info("Updating OCN version details from platform | Tenant ID: {} | Platform URL: {}",
                    tenantId, platformConfiguration.getPlatformUrl());
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
                log.error("Tenant ID: {} | {}", tenantId, msg);
                throw new OCPICustomException(msg);
            }
            if (responseBody.getStatus_code() != 1000) {
                String msg = String.format(
                        "Platform error retrieving OCN version details: %s (status_code %d, HTTP %d)",
                        responseBody.getStatus_message(), responseBody.getStatus_code(), httpStatus);
                log.error("Tenant ID: {} | {}", tenantId, msg);
                throw new OCPICustomException(msg);
            }
            OcnVersionDetails details = responseBody.getData();
            if (details == null) {
                String msg = String.format(
                        "Platform returned no data for OCN version details (status_code %d, HTTP %d)",
                        responseBody.getStatus_code(), httpStatus);
                log.error("Tenant ID: {} | {}", tenantId, msg);
                throw new OCPICustomException(msg);
            }
            platformConfiguration.setOcnVersionDetails(tenantId, details);
        } catch (RestClientResponseException rex) {
            String body = rex.getResponseBodyAsString();
            String msg = String.format(
                    "HTTP %d from platform while retrieving OCN version details: %s",
                    rex.getStatusCode().value(), body);
            log.error("Tenant ID: {} | {}", tenantId, msg);
            throw new OCPICustomException(msg);
        } catch (NullPointerException npe) {
            String msg = "Failed to store OCN version details: platform configuration storage not initialized";
            log.error("Tenant ID: {} | {}", tenantId, msg);
            throw new OCPICustomException(msg);
        } catch (Exception ex) {
            log.error(
                    "Tenant ID: {} | Error while retrieving or updating OCN version details from platform, error message: {}",
                    tenantId, ex.getLocalizedMessage());
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
        if (platformConfiguration.getOcnVersionDetails().get(tenantId) == null) {
            throw new IllegalArgumentException("Tenant not found in OcnVersionDetails");
        }

        Endpoint endpoint = platformConfiguration.getOcnVersionDetails().get(tenantId).getEndpoints().stream()
                .filter(e -> e.getIdentifier() == moduleID && e.getRole() == interfaceRole)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
        String url = endpoint.getUrl();
        int startIndex = url.indexOf("ocpi/");
        String endpointUrl = url.substring(startIndex);
        return platformConfiguration.getPlatformUrl() + "/api/v1/internal/" + tenantId + "/outflow/"
                + endpointUrl;

    }

    private String getOcnVersionDetailsUrl(String tenantId) {
        return platformConfiguration.getPlatformUrl() + "/" + tenantId + "/ocpi/ocn-version-details";
    }

}
