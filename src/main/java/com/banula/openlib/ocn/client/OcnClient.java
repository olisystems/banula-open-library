package com.banula.openlib.ocn.client;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.banula.openlib.ocn.Notary;
import com.banula.openlib.ocn.model.BasicRole;
import com.banula.openlib.ocn.model.CreatePlatformRequest;
import com.banula.openlib.ocn.model.CreatePlatformResponse;
import com.banula.openlib.ocn.model.OcnClientConfiguration;
import com.banula.openlib.ocn.model.OcnCredential;
import com.banula.openlib.ocn.model.ValuesToSign;
import com.banula.openlib.ocpi.model.OcpiErrorResponse;
import com.banula.openlib.ocpi.model.OcpiResponse;
import com.banula.openlib.ocpi.model.Version;
import com.banula.openlib.ocpi.model.VersionDetails;
import com.banula.openlib.ocpi.model.dto.CredentialsDTO;
import com.banula.openlib.ocpi.model.enums.Role;
import com.banula.openlib.ocpi.model.enums.VersionNumber;
import com.banula.openlib.ocpi.model.vo.BusinessDetails;
import com.banula.openlib.ocpi.model.vo.CredentialsRole;
import com.banula.openlib.ocpi.model.vo.Endpoint;
import com.banula.openlib.ocpi.util.InfoUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OcnClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private OcnCredentialHandler ocnCredentialHandler;
    private RestTemplate restTemplate;
    private OcnVersionDetailsHandler ocnVersionDetailsHandler;

    public static OcnClientConfiguration configuration;

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
        try {
            String generatedTokenC = null;

            // If TokenC exists, try GET credentials and then update via PUT
            if (credential != null && credential.getTokenC() != null && !credential.getTokenC().isBlank()) {
                log.info("Token C found, verifying credentials on OCN Node...");
                CredentialsDTO currentCredentials = this.getCredentials(credential.getTokenC());

                if (currentCredentials != null) {
                    log.info("Party is already registered in OCN Node, updating credentials...");
                    generatedTokenC = this.updateParty(configuration.getPartyBackendUrl(),
                            configuration.getOcpiRoles());
                } else {
                    log.warn("Failed to get credentials from OCN Node | Proceeding with fresh registration...");
                }
            }

            // If no TokenC or GET failed, delete and register anew
            if (generatedTokenC == null) {
                log.info("Performing fresh registration...");
                this.deleteCredentials();
                generatedTokenC = this.registerParty(configuration.getPartyBackendUrl(), configuration.getOcpiRoles());
            }

            verifyAndSetGeneratedTokenC(generatedTokenC);
            VersionDetails endpointResponse = this.getVersionDetails();

            log.info("OCN Node: Ocpi Version: {} | Endpoints: {}", endpointResponse.getVersion(),
                    endpointResponse.getEndpoints().size());
            for (Endpoint endpoint : endpointResponse.getEndpoints()) {
                log.info("Endpoint: {}", endpoint);
            }
            ocnVersionDetailsHandler.saveVersionDetails(endpointResponse);

        } catch (Exception ex) {
            log.error(String.format("OCN party registration error: %s", ex.getLocalizedMessage()));
        }
    }

    private void verifyAndSetGeneratedTokenC(String tokenC) {
        if (tokenC == null || tokenC.isBlank()) {
            log.error("Error while registering party, Ocn Node CREDENTIALS endpoint returned a null tokenC");
        } else {
            configuration.setTokenC(tokenC);
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

        // Create platform and get response
        CreatePlatformResponse platformResponse = this.createPlatform();

        // Save token A
        ocnCredential.setTokenA(platformResponse.getToken());
        configuration.setTokenA(platformResponse.getToken());

        // Retrieve credentials URL from versions endpoint
        String credentialsUrl = this.retrieveCredentialsUrl(platformResponse.getVersions());

        // Register party
        String credentialsTokenC = this.registerPartyCredentials(platformResponse.getToken(), backendUrl, roles,
                HttpMethod.POST, credentialsUrl);
        if (credentialsTokenC == null) {
            return null;
        }
        // Set new tokenC
        configuration.setTokenC(credentialsTokenC);
        ocnCredentialHandler.saveOcnCredential(ocnCredential);

        return credentialsTokenC;
    }

    public String updateParty(String backendUrl, List<Role> roles) throws Exception {
        OcnCredential credentials = ocnCredentialHandler.getOcnCredential();

        if (credentials == null)
            throw new Exception("Error while trying to update party, Token C not found.");

        // Get current tokenC
        String registrationToken = credentials.getTokenC();

        // Retrieve credentials URL from versions endpoint
        String credentialsUrl = this.retrieveCredentialsUrl(OcnEndpoints.VERSIONS.toString());

        // Update party
        String credentialsTokenC = this.registerPartyCredentials(registrationToken, backendUrl, roles,
                HttpMethod.PUT, credentialsUrl);
        if (credentialsTokenC == null) {
            return null;
        }
        // Update tokenC
        configuration.setTokenC(credentialsTokenC);
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
        String finalUrl = uriBuilder.encode().toUriString();

        // Log the curl command
        logCurlCommand(finalUrl, httpMethod, headers, requestBody);

        ResponseEntity<T> response = restTemplate.exchange(
                finalUrl,
                httpMethod,
                entity,
                responseTypeRef);

        T responseBody = response.getBody();
        if (responseBody instanceof OcpiResponse) {
            ((OcpiResponse<?>) responseBody).setHeaders(response.getHeaders());
        }
        return responseBody;
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
        String finalUrl = uriBuilder.encode().toUriString();

        // Log the curl command
        logCurlCommand(finalUrl, httpMethod, headers, requestBody);

        ResponseEntity<T> response = restTemplate.exchange(
                finalUrl,
                httpMethod,
                entity,
                responseTypeRef);

        T responseBody = response.getBody();
        if (responseBody instanceof OcpiResponse) {
            ((OcpiResponse<?>) responseBody).setHeaders(response.getHeaders());
        }
        return responseBody;
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

    /**
     * Logs the exact curl command that would be executed for debugging purposes
     * 
     * @param url         The complete URL including query parameters
     * @param httpMethod  The HTTP method (GET, POST, PUT, DELETE, etc.)
     * @param headers     The HTTP headers
     * @param requestBody The request body as a JSON string
     */
    private void logCurlCommand(String url, HttpMethod httpMethod, HttpHeaders headers, String requestBody) {
        if (!configuration.isLogCurlCommand()) {
            return;
        }
        InfoUtils.logCurlCommand(url, httpMethod, headers, requestBody);
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
     * Retrieves a list of available OCPI versions from the OCN node
     * 
     * @return List of Version objects representing available OCPI versions and
     *         their detail URLs
     * @throws Exception if communication fails
     */
    public List<Version> getVersions() throws Exception {
        try {
            // Define the response type (a wrapper containing a list of versions)
            ParameterizedTypeReference<OcpiResponse<List<Version>>> responseTypeRef = new ParameterizedTypeReference<OcpiResponse<List<Version>>>() {
            };

            // Make the HTTP request using TokenA for authorization
            HttpHeaders headers = this.createHeadersAuthTokenA();
            OcpiResponse<List<Version>> response = this._call(
                    OcnEndpoints.VERSIONS,
                    null,
                    new HashMap<>(),
                    headers,
                    responseTypeRef,
                    HttpMethod.GET,
                    new ArrayList<>(),
                    null);

            // Return the versions from the response
            if (response != null && response.getData() != null) {
                return response.getData();
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            log.error("Failed to retrieve OCPI versions: {}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Retrieves a list of available OCPI endpoints from the OCN node
     * 
     * @return List of Endpoint objects representing available OCPI modules and
     *         their URLs
     * @throws Exception if communication fails
     */
    public VersionDetails getVersionDetails() throws Exception {
        try {
            // Get versions list first
            List<Version> versions = this.getVersions();

            // Find the 2.2.1 version URL
            String versionDetailsUrl = null;
            for (Version version : versions) {
                if (version.getVersion() != null && VersionNumber.V_2_2_1.equals(version.getVersion())) {
                    versionDetailsUrl = version.getUrl();
                    break;
                }
            }

            if (versionDetailsUrl == null) {
                throw new Exception("Version 2.2.1 not found in versions list");
            }

            // Define the response type (a wrapper containing version details)
            ParameterizedTypeReference<OcpiResponse<VersionDetails>> responseTypeRef = new ParameterizedTypeReference<OcpiResponse<VersionDetails>>() {
            };

            // Make the HTTP request using TokenC for authorization
            HttpHeaders headers = this.createHeaders();
            OcpiResponse<VersionDetails> response = this._call(versionDetailsUrl, null, new HashMap<>(), headers,
                    responseTypeRef, HttpMethod.GET, new ArrayList<>(), null);

            // Return the endpoints from the response
            if (response != null && response.getData() != null) {
                return response.getData();
            }
            return new VersionDetails();
        } catch (Exception ex) {
            log.error("Failed to retrieve OCPI endpoints: {}", ex.getMessage());
            throw ex;
        }
    }

    // By default use TokenC
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        String base64TokenC = Base64.getEncoder().encodeToString(configuration.getTokenC().getBytes());
        headers.add("Authorization", String.format("Token %s", base64TokenC));
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Correlation-ID", UUID.randomUUID().toString());
        headers.set("OCPI-from-country-code", configuration.getFromCountryCode());
        headers.set("OCPI-from-party-id", configuration.getFromPartyId());
        headers.set("OCPI-to-country-code", configuration.getToCountryCode());
        headers.set("OCPI-to-party-id", configuration.getToPartyId());
        return headers;
    }

    private HttpHeaders createHeadersAuthTokenA() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        String base64TokenA = Base64.getEncoder().encodeToString(configuration.getTokenA().getBytes());
        headers.add("Authorization", String.format("Token %s", base64TokenA));
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Correlation-ID", UUID.randomUUID().toString());
        headers.set("OCPI-from-country-code", configuration.getFromCountryCode());
        headers.set("OCPI-from-party-id", configuration.getFromPartyId());
        headers.set("OCPI-to-country-code", configuration.getToCountryCode());
        headers.set("OCPI-to-party-id", configuration.getToPartyId());
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

    public String deleteCredentials() {
        try {
            String url = String.format("%s%s/%s/%s", configuration.getNodeUrl(),
                    OcnEndpoints.DELETE_PARTY_CREDENTIALS.toString(),
                    configuration.getFromCountryCode(), configuration.getFromPartyId());

            String base64AdminKey = Base64.getEncoder().encodeToString(configuration.getAdminKey().getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Token " + base64AdminKey);

            ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
            };

            return this._call(url, null, new HashMap<>(), headers, responseType, HttpMethod.DELETE, new ArrayList<>(),
                    null);
        } catch (Exception ex) {
            log.error("Error while deleting credentials on OCN Node: " + ex.getMessage());
            return null;
        }
    }

    private CredentialsDTO getCredentials(String tokenC) {
        try {
            String base64TokenC = Base64.getEncoder().encodeToString(tokenC.getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Token " + base64TokenC);

            ParameterizedTypeReference<OcpiResponse<CredentialsDTO>> responseType = new ParameterizedTypeReference<OcpiResponse<CredentialsDTO>>() {
            };

            // Retrieve credentials URL from versions endpoint
            String credentialsUrl = this.retrieveCredentialsUrl(OcnEndpoints.VERSIONS.toString());

            OcpiResponse<CredentialsDTO> response = this._call(
                    credentialsUrl,
                    null,
                    new HashMap<>(),
                    headers,
                    responseType,
                    HttpMethod.GET,
                    new ArrayList<>(),
                    null);

            if (response == null || response.getData() == null) {
                log.warn("Could not retrieve credentials: empty response or missing data");
                return null;
            }
            return response.getData();
        } catch (Exception e) {
            log.warn("Could not retrieve credentials: " + e.getMessage());
            return null;
        }
    }

    private CreatePlatformResponse createPlatform() throws Exception {
        List<BasicRole> roles = List
                .of(new BasicRole(configuration.getFromPartyId(), configuration.getFromCountryCode()));
        CreatePlatformRequest request = new CreatePlatformRequest();
        request.setRoles(roles);

        String base64AdminKey = Base64.getEncoder().encodeToString(configuration.getAdminKey().getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + base64AdminKey);

        ParameterizedTypeReference<CreatePlatformResponse> responseType = new ParameterizedTypeReference<CreatePlatformResponse>() {
        };
        CreatePlatformResponse response = this._call(
                OcnEndpoints.CREATE_PLATFORM,
                request,
                new HashMap<>(),
                headers,
                responseType, HttpMethod.POST,
                new ArrayList<>(),
                null);

        return response;
    }

    private String retrieveCredentialsUrl(String versionsUrl) throws Exception {
        try {

            VersionDetails versionDetails = getVersionDetails();
            if (versionDetails.getEndpoints() == null) {
                throw new Exception("No endpoints found in version details");
            }

            // Find credentials endpoint
            for (Endpoint endpoint : versionDetails.getEndpoints()) {
                if ("credentials".equals(endpoint.getIdentifier()) && "SENDER".equals(endpoint.getRole().toString())) {
                    return endpoint.getUrl();
                }
            }

            throw new Exception("Credentials endpoint not found in version details");
        } catch (Exception e) {
            log.error("Error retrieving credentials URL from versions endpoint: " + e.getMessage());
            throw e;
        }
    }

    private CredentialsRole createCredentialsRole(Role role) {
        BusinessDetails businessDetails = new BusinessDetails();
        businessDetails.setName(String.format("Transit %s %s | %s", configuration.getFromPartyId(),
                configuration.getFromCountryCode(), role.toString()));
        return new CredentialsRole(role, configuration.getFromPartyId(), configuration.getFromCountryCode(),
                businessDetails);
    }

    public String registerPartyCredentials(String registrationToken, String backendUrl, List<Role> roles,
            HttpMethod httpMethod, String credentialUrl) throws Exception {

        List<CredentialsRole> credentialsRoles = new ArrayList<>();
        for (Role role : roles) {
            credentialsRoles.add(createCredentialsRole(role));
        }
        CredentialsDTO request = new CredentialsDTO(configuration.getTokenB(), backendUrl, credentialUrl,
                credentialsRoles);
        String base64RegistrationToken = Base64.getEncoder().encodeToString(registrationToken.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + base64RegistrationToken);

        ParameterizedTypeReference<OcpiResponse<CredentialsDTO>> responseType = new ParameterizedTypeReference<OcpiResponse<CredentialsDTO>>() {
        };
        try {
            OcpiResponse<CredentialsDTO> response = this._call(
                    credentialUrl,
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
