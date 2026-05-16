package com.banula.openlib.ocpi.platform;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.banula.openlib.ocn.client.GenericTypeRefUtil;
import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.model.OcpiResponse;
import com.banula.openlib.ocpi.model.enums.InterfaceRole;
import com.banula.openlib.ocpi.model.enums.ModuleID;
import com.banula.openlib.ocpi.model.enums.Role;
import com.banula.openlib.ocpi.util.InfoUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
// TODO: move it to privatelib once ClientHubInfo module is moved drom NSP to
// platform-banula
public class PlatformClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private final RestTemplate restTemplate;
    private final PlatformConfiguration platformConfiguration;
    private final Validator validator;

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
        validateOcpiCompliance(body, method);
        return executeOutflowRequest(tenantId, toOcpiCountryCode, toOcpiPartyId, interfaceRole, moduleID, method, body,
                responseTypeRef, pathVariables, queryParams);
    }

    private <N> void validateOcpiCompliance(N body, HttpMethod method) {
        if (!HttpMethod.PUT.equals(method)) {
            return;
        }
        if (body == null || !body.getClass().getPackageName().startsWith("com.banula.openlib.ocpi.model.dto")) {
            throw new OCPICustomException(
                    "PUT request body must be a valid OCPI DTO from com.banula.openlib.ocpi.model.dto");
        }
        Set<ConstraintViolation<N>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            String details = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            throw new OCPICustomException("OCPI object validation failed: " + details);
        }
    }

    private <T, N> OcpiResponse<T> executeOutflowRequest(String tenantId, String toOcpiCountryCode,
            String toOcpiPartyId, InterfaceRole interfaceRole, ModuleID moduleID, HttpMethod method, N body,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef, List<String> pathVariables,
            java.util.Map<String, String> queryParams) {
        log.info("Sending outflow request to platform for country code: {} and party id: {}", toOcpiCountryCode,
                toOcpiPartyId);
        try {

            String[] from = parseTenantId(tenantId);
            HttpHeaders headers = createHeaders(from[0], from[1], toOcpiCountryCode, toOcpiPartyId);
            String platformEndpoint = getOutflowUrl(moduleID, interfaceRole);
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

    private HttpHeaders createHeaders(String fromOcpiCountryCode, String fromOcpiPartyId, String toOcpiCountryCode,
            String toOcpiPartyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Correlation-ID", UUID.randomUUID().toString());
        headers.set("OCPI-from-country-code", fromOcpiCountryCode);
        headers.set("OCPI-from-party-id", fromOcpiPartyId);
        headers.set("OCPI-to-country-code", toOcpiCountryCode);
        headers.set("OCPI-to-party-id", toOcpiPartyId);
        return headers;
    }

    private String[] parseTenantId(String tenantId) {
        if (tenantId == null || !tenantId.matches("^[A-Za-z]{2}_[A-Za-z0-9]{3}$")) {
            throw new IllegalArgumentException(
                    "Invalid tenantId format: '" + tenantId + "'. Expected <countryCode>_<partyId> (e.g. DE_ABC)");
        }
        return tenantId.split("_", 2);
    }

    public String getOutflowUrl(ModuleID moduleID, InterfaceRole interfaceRole) {
        String role = interfaceRole.name().toLowerCase();
        return platformConfiguration.getPlatformUrl()
                + "/api/v1/internal//outflow/ocpi/" + role
                + "/" + platformConfiguration.getOcpiVersion()
                + "/" + moduleID.value();
    }

    public boolean verifyOcnCredentials() {
        String url = platformConfiguration.getPlatformUrl() + "/admin/ocn-node/verify";
        ResponseEntity<java.util.Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<java.util.Map<String, Object>>() {
                });
        java.util.Map<String, Object> body = response.getBody();
        return body != null && Boolean.TRUE.equals(body.get("is_valid"));
    }

    public boolean verifyThereAreRoleTenantsByRole(Role role) {
        String url = platformConfiguration.getPlatformUrl() + "/admin/tenant/droplist/" + role.name();
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> tenants = response.getBody();
        return tenants != null && !tenants.isEmpty();
    }

    public List<String> findTenantIdsByRole() {
        Role role = platformConfiguration.getOcpiRole();
        String url = platformConfiguration.getPlatformUrl() + "/admin/tenant/droplist/" + role.name();
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> tenants = response.getBody();
        return tenants != null ? tenants : List.of();
    }

}
