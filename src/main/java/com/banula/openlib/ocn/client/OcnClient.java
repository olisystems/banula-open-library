package com.banula.openlib.ocn.client;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.banula.openlib.ocn.model.OcnClientConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.banula.openlib.ocn.Notary;
import com.banula.openlib.ocn.model.*;
import com.banula.openlib.ocpi.model.OcpiErrorResponse;
import com.banula.openlib.ocpi.model.OcpiResponse;
import com.banula.openlib.ocpi.model.dto.CredentialsDTO;
import com.banula.openlib.ocpi.model.enums.Role;
import com.banula.openlib.ocpi.model.vo.BusinessDetails;
import com.banula.openlib.ocpi.model.vo.CredentialsRole;
import com.banula.openlib.ocpi.model.vo.Endpoint;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
public class OcnClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private OcnCredentialHandler ocnCredentialHandler;
    private RestTemplate restTemplate;
    private OcnVersionDetailsHandler ocnVersionDetailsHandler;

    static OcnClientConfiguration configuration;

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
        // avoids reading dates as array
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public OcnClient(OcnClientConfiguration initialConfiguration, OcnCredentialHandler ocnCredentialHandler,
            OcnVersionDetailsHandler ocnVersionDetailsHandler) {
        this.restTemplate = new RestTemplate();
        this.ocnCredentialHandler = ocnCredentialHandler;
        this.ocnVersionDetailsHandler = ocnVersionDetailsHandler;
        configuration = initialConfiguration;
        HttpClient httpClient = HttpClientBuilder.create().build();
        // necessary to allow PATCH requests
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate.setRequestFactory(requestFactory);
    }

    public void shakeHands() {
        log.info("Party Configuration: {} {} {} | url: {}", configuration.getOcpiRoles(),
                configuration.getFromCountryCode(), configuration.getFromPartyId(), configuration.getPartyBackendUrl());
        log.info("Initiating communication with the ocn-node: {}", configuration.getNodeUrl());
        OcnCredential credential = ocnCredentialHandler.getOcnCredential();
        boolean isToUpdateParty = configuration.isUpdatingParty();
        try {
            String generatedTokenC = null;
            // if no token C is found, register party and save in database
            if (credential == null || credential.getTokenC() == null || credential.getTokenC().isBlank()) {
                log.info("Token C not found, registering party...");
                generatedTokenC = this.registerParty(configuration.getPartyBackendUrl(), configuration.getOcpiRoles());
            } else {
                // if token C is found, update party in case of isToUpdateParty = true, and save
                // in database
                if (isToUpdateParty) {
                    log.info("Updating party credentials...");
                    generatedTokenC = this.updateParty(configuration.getPartyBackendUrl(),
                            configuration.getOcpiRoles());
                } else {
                    generatedTokenC = credential.getTokenC();
                    log.info("Token C found, skipping party registration");
                }
            }
            verifyAndSetGeneratedTokenC(generatedTokenC);
            OcnVersionDetails endpointResponse = this.getVersionDetails();

            log.info("OCN Node: Ocpi Version: {} | Endpoints: {}", endpointResponse.getVersion(),
                    endpointResponse.getEndpoints().size());
            for (Endpoint endpoint : endpointResponse.getEndpoints()) {
                log.info("Endpoint: {}", endpoint);
            }
            ocnVersionDetailsHandler.saveVersionDetails(endpointResponse);

        } catch (Exception ex) {
            log.error(String.format("OCN party registration error: %s", ex.getLocalizedMessage()));
            for (StackTraceElement ste : ex.getStackTrace()) {
                System.out.println(ste);
            }
        }
    }

    private void verifyAndSetGeneratedTokenC(String tokenC) {
        if (tokenC == null || tokenC.isBlank()) {
            log.error("Error while registering party, Ocn Node CREDENTIALS endpoint returned a null tokenC");
        } else {
            configuration.setTokenC(tokenC);
            configuration.setBase64TokenC(Base64.getEncoder().encodeToString(tokenC.getBytes()));
            log.info(String.format(
                    "%s updated successfully, you can set 'updating-party = false' to avoid future re-shaking hands",
                    configuration.getOcpiRoles().toString()));
            log.info("Generated Token C: {}", tokenC);
        }
    }

    public String registerParty(String backendUrl, List<Role> roles) throws Exception {
        OcnCredential ocnCredential = new OcnCredential();
        ocnCredential.setCountryCode(configuration.getFromCountryCode());
        ocnCredential.setPartyId(configuration.getFromPartyId());

        // Get registration token
        String tokenA = this.getRegistrationToken();

        // Save token A
        ocnCredential.setTokenA(tokenA);

        // Register party
        String credentialsTokenC = this.registerPartyCredentials(tokenA, backendUrl, roles, HttpMethod.POST);
        if (credentialsTokenC == null) {
            return null;
        }
        // Set new tokenC
        configuration.setTokenC(credentialsTokenC);
        configuration.setBase64TokenC(Base64.getEncoder().encodeToString(credentialsTokenC.getBytes()));
        ocnCredential.setTokenC(credentialsTokenC);
        ocnCredentialHandler.saveOcnCredential(ocnCredential);

        return credentialsTokenC;
    }

    public String updateParty(String backendUrl, List<Role> roles) throws Exception {
        OcnCredential credentials = ocnCredentialHandler.getOcnCredential();

        if (credentials == null)
            throw new Exception("Error while trying to update party, Token C not found.");

        // Get current tokenC
        String registrationToken = credentials.getTokenC();
        // Update party
        String credentialsTokenC = this.registerPartyCredentials(registrationToken, backendUrl, roles,
                HttpMethod.PUT);
        if (credentialsTokenC == null) {
            return null;
        }
        // Update tokenC
        configuration.setTokenC(credentialsTokenC);
        configuration.setBase64TokenC(Base64.getEncoder().encodeToString(credentialsTokenC.getBytes()));
        credentials.setTokenC(credentialsTokenC);
        ocnCredentialHandler.saveOcnCredential(credentials);

        return credentialsTokenC;
    }

    public String getCurrentOcnNodeUrl() {
        return configuration.getNodeUrl();
    }

    public <T, N> OcpiResponse<T> executeOcpiOperation(OcnEndpoints f, N body, Class<T> refType) throws Exception {
        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        HttpHeaders headers = this.createHeaders();
        return this._call(f, body, new HashMap<>(), headers, responseTypeRef, HttpMethod.POST, new ArrayList<>(), null);
    }

    // to keep compliance with badenova-cpo
    public <T, N> OcpiResponse<T> executeOcpiOperation(OcnEndpoints endpoint, N body, String toPartyId,
            String toCountryCode, Class<T> refType) throws Exception {
        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(endpoint, body, new HashMap<>(), headers, responseTypeRef,
                HttpMethod.POST, new ArrayList<>(), null);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    public <T, N> OcpiResponse<T> executeOcpiOperation(OcnEndpoints endpoint, N body, String toPartyId,
            String toCountryCode, Class<T> refType, HttpMethod httpMethod, List<String> pathVariables)
            throws Exception {
        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(endpoint, body, new HashMap<>(), headers, responseTypeRef, httpMethod,
                pathVariables, null);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    public <T, N> OcpiResponse<T> executeOcpiOperation(OcnEndpoints endpoint, N body, String toPartyId,
            String toCountryCode,
            Class<T> refType, HttpMethod httpMethod, List<String> pathVariables,
            HashMap<String, String> requestParameters) throws Exception {
        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(endpoint, body, new HashMap<>(), headers, responseTypeRef, httpMethod,
                pathVariables, requestParameters);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    private <T, N> T _call(OcnEndpoints endpoint, N body, HashMap<String, String> params, HttpHeaders headers,
            ParameterizedTypeReference<T> responseTypeRef, HttpMethod httpMethod, List<String> pathVariables,
            HashMap<String, String> requestParameters) throws Exception {
        addSignatureIfSupported(headers, body, params);
        String requestBody = objectMapper.writeValueAsString(body);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(configuration.getNodeUrl() + endpoint.toString());
        addPathAndQueryParams(uriBuilder, pathVariables, requestParameters);
        ResponseEntity<T> response = restTemplate.exchange(
                uriBuilder.encode().toUriString(),
                httpMethod,
                entity,
                responseTypeRef);

        return response.getBody();
    }

    public <T, N> OcpiResponse<T> executeOcpiOperation(String url, N body, String toPartyId, String toCountryCode,
            Class<T> refType, HttpMethod httpMethod, List<String> pathVariables) throws Exception {
        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(url, body, new HashMap<>(), headers, responseTypeRef, httpMethod,
                pathVariables, null);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    // Added this method to support cases that use a custom URL with Class<T> and
    // request parameters
    public <T, N> OcpiResponse<T> executeOcpiOperation(String url, N body, String toPartyId, String toCountryCode,
            Class<T> refType, HttpMethod httpMethod, List<String> pathVariables,
            HashMap<String, String> requestParameters) throws Exception {
        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef = GenericTypeRefUtil.getWrapperTypeRef(refType);
        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(url, body, new HashMap<>(), headers, responseTypeRef, httpMethod,
                pathVariables, requestParameters);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    // created due to CommandResult that must be sent for a complete url informed by
    // OCN (StartSession.responseUrl) instead of a predetermined endpoint
    private <T, N> T _call(String url, N body, HashMap<String, String> params, HttpHeaders headers,
            ParameterizedTypeReference<T> responseTypeRef, HttpMethod httpMethod, List<String> pathVariables,
            HashMap<String, String> requestParameters) throws Exception {
        addSignatureIfSupported(headers, body, params);
        String requestBody = objectMapper.writeValueAsString(body);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        addPathAndQueryParams(uriBuilder, pathVariables, requestParameters);
        ResponseEntity<T> response = restTemplate.exchange(
                uriBuilder.encode().toUriString(),
                httpMethod,
                entity,
                responseTypeRef);

        return response.getBody();
    }

    private void addPathAndQueryParams(UriComponentsBuilder uriBuilder, List<String> pathVariables,
            HashMap<String, String> requestParameters) {
        // Add path variables in request
        for (String pathVariable : pathVariables) {
            uriBuilder.pathSegment(pathVariable);
        }

        // Add request params in request
        if (requestParameters != null) {
            for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
    }

    private <N> void addSignatureIfSupported(HttpHeaders headers, N body, HashMap<String, String> params)
            throws Exception {
        if (configuration.isSigningSupported()) {
            ValuesToSign<N> valuesToSign = new ValuesToSign<>(headers.toSingleValueMap(), body, params);
            String signature = this.signRequest(valuesToSign);
            headers.set("OCN-Signature", signature);
        }
    }

    // Added this additional method to support cases that return a list of objects
    public <T, N> OcpiResponse<T> executeOcpiOperation(
            OcnEndpoints endpoint,
            N body,
            String toPartyId,
            String toCountryCode,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef,
            HttpMethod httpMethod,
            List<String> pathVariables) throws Exception {
        return this.executeOcpiOperation(endpoint, body, toPartyId, toCountryCode, responseTypeRef, httpMethod,
                pathVariables, null);
    }

    // Added this additional method to support cases that return a list of objects
    public <T, N> OcpiResponse<T> executeOcpiOperation(
            OcnEndpoints endpoint,
            N body,
            String toPartyId,
            String toCountryCode,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef,
            HttpMethod httpMethod,
            List<String> pathVariables,
            HashMap<String, String> requestParameters) throws Exception {

        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(endpoint, body, new HashMap<>(), headers, responseTypeRef, httpMethod,
                pathVariables, requestParameters);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    // Added this method to support cases that use a custom URL instead of
    // OcnEndpoints
    public <T, N> OcpiResponse<T> executeOcpiOperation(
            String url,
            N body,
            String toPartyId,
            String toCountryCode,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef,
            HttpMethod httpMethod,
            List<String> pathVariables,
            HashMap<String, String> requestParameters) throws Exception {

        String currentCountryCode = configuration.getToCountryCode();
        String currentPartyId = configuration.getToPartyId();

        configuration.setToCountryCode(toCountryCode);
        configuration.setToPartyId(toPartyId);

        HttpHeaders headers = this.createHeaders();
        OcpiResponse<T> response = this._call(url, body, new HashMap<>(), headers, responseTypeRef, httpMethod,
                pathVariables, requestParameters);

        configuration.setToCountryCode(currentCountryCode);
        configuration.setToPartyId(currentPartyId);

        return response;
    }

    // Added this method to support cases that use a custom URL with no request
    // parameters
    public <T, N> OcpiResponse<T> executeOcpiOperation(
            String url,
            N body,
            String toPartyId,
            String toCountryCode,
            ParameterizedTypeReference<OcpiResponse<T>> responseTypeRef,
            HttpMethod httpMethod,
            List<String> pathVariables) throws Exception {
        return this.executeOcpiOperation(url, body, toPartyId, toCountryCode, responseTypeRef, httpMethod,
                pathVariables, null);
    }

    /**
     * Retrieves a list of available OCPI endpoints from the OCN node
     * 
     * @return List of Endpoint objects representing available OCPI modules and
     *         their URLs
     * @throws Exception if communication fails
     */
    public OcnVersionDetails getVersionDetails() throws Exception {
        try {
            // Create the URL for the endpoints resource
            String url = String.format("%s/ocpi/2.2.1", configuration.getNodeUrl());

            // Define the response type (a wrapper containing a list of endpoints)
            ParameterizedTypeReference<OcpiResponse<OcnVersionDetails>> responseTypeRef = new ParameterizedTypeReference<OcpiResponse<OcnVersionDetails>>() {
            };

            // Make the HTTP request
            HttpHeaders headers = this.createHeaders();
            OcpiResponse<OcnVersionDetails> response = this._call(url, null, new HashMap<>(), headers,
                    responseTypeRef, HttpMethod.GET, new ArrayList<>(), null);

            // Return the endpoints from the response
            if (response != null && response.getData() != null) {
                return response.getData();
            }
            return new OcnVersionDetails();
        } catch (Exception ex) {
            log.error("Failed to retrieve OCPI endpoints: {}", ex.getMessage());
            throw ex;
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        headers.add("Authorization", String.format("Token %s", configuration.getTokenC()));
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Correlation-ID", UUID.randomUUID().toString());
        headers.set("OCPI-from-country-code", configuration.getFromCountryCode());
        headers.set("OCPI-from-party-id", configuration.getFromPartyId());
        headers.set("OCPI-to-country-code", configuration.getToCountryCode());
        headers.set("OCPI-to-party-id", configuration.getToPartyId());
        headers.set("Authorization", "Token " + configuration.getBase64TokenC());
        return headers;
    }

    public <T> String signRequest(ValuesToSign<T> request) throws Exception {
        Notary notary = new Notary();
        notary.sign(request, configuration.getPrivateKey());
        return notary.serialize();
    }

    public static <T> String signResponse(OcpiResponse<T> response) throws Exception {
        if (configuration != null && configuration.isSigningSupported()) {
            ValuesToSign<T> request = new ValuesToSign<>(new HashMap<>(), response.getData(), new HashMap<>());
            Notary notary = new Notary();
            notary.sign(request, configuration.getPrivateKey());
            return notary.serialize();
        }

        return "";
    }

    public static String signErrorResponse(OcpiErrorResponse error) throws Exception {
        if (configuration != null && configuration.isSigningSupported()) {
            ValuesToSign<OcpiErrorResponse> request = new ValuesToSign<>(new HashMap<>(), error, new HashMap<>());
            Notary notary = new Notary();
            notary.sign(request, configuration.getPrivateKey());
            return notary.serialize();
        }

        return "";
    }

    private String getRegistrationToken() throws Exception {
        List<OcnRegistrationRequest> request = List
                .of(new OcnRegistrationRequest(configuration.getFromCountryCode(), configuration.getFromPartyId()));
        String base64AdminKey = Base64.getEncoder().encodeToString(configuration.getAdminKey().getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + base64AdminKey);

        ParameterizedTypeReference<OcnRegistrationResponse> responseType = new ParameterizedTypeReference<OcnRegistrationResponse>() {
        };
        OcnRegistrationResponse response = this._call(
                OcnEndpoints.GENERATE_REGISTRATION_TOKEN,
                request,
                new HashMap<>(),
                headers,
                responseType, HttpMethod.POST,
                new ArrayList<>(),
                null);

        return response.getToken();
    }

    private CredentialsRole createCredentialsRole(Role role) {
        BusinessDetails businessDetails = new BusinessDetails();
        businessDetails.setName(String.format("Transit %s %s | %s", configuration.getFromPartyId(),
                configuration.getFromCountryCode(), role.toString()));
        return new CredentialsRole(role, configuration.getFromPartyId(), configuration.getFromCountryCode(),
                businessDetails);
    }

    public String registerPartyCredentials(String registrationToken, String backendUrl, List<Role> roles,
            HttpMethod httpMethod) throws Exception {

        List<CredentialsRole> credentialsRoles = new ArrayList<>();
        for (Role role : roles) {
            credentialsRoles.add(createCredentialsRole(role));
        }
        CredentialsDTO request = new CredentialsDTO(configuration.getTokenB(), backendUrl, credentialsRoles);
        String base64RegistrationToken = Base64.getEncoder().encodeToString(registrationToken.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + base64RegistrationToken);

        ParameterizedTypeReference<OcpiResponse<CredentialsDTO>> responseType = new ParameterizedTypeReference<OcpiResponse<CredentialsDTO>>() {
        };
        try {
            OcpiResponse<CredentialsDTO> response = this._call(
                    OcnEndpoints.REGISTER_PARTY_CREDENTIALS,
                    request,
                    new HashMap<>(),
                    headers,
                    responseType,
                    httpMethod,
                    new ArrayList<>(),
                    null);

            return response.getData() == null ? null : response.getData().getToken();
        } catch (Exception e) {
            String msg = "Error while registering party credentials: " + e.getMessage();
            log.error(msg);
            return null;
        }
    }

    public OcnClientConfiguration getConfiguration() {
        return configuration;
    }

    public static OcnClientConfiguration getStaticConfiguration() {
        return configuration;
    }
}
