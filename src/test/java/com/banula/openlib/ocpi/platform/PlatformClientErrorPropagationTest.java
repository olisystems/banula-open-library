package com.banula.openlib.ocpi.platform;

import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.exception.OcpiUpstreamException;
import com.banula.openlib.ocpi.model.OcpiResponse;
import com.banula.openlib.ocpi.model.enums.InterfaceRole;
import com.banula.openlib.ocpi.model.enums.ModuleID;
import com.banula.openlib.ocpi.model.enums.Role;
import com.banula.openlib.ocpi.model.enums.VersionNumber;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PlatformClientErrorPropagationTest {

    private static final String URL = "http://platform/api/v1/internal/outflow/ocpi/receiver/2.2.1/tokens/DE/OLI/T1";

    private MockRestServiceServer server;
    private PlatformClient client;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        PlatformConfiguration config = new PlatformConfiguration() {
            public String getPlatformUrl() {
                return "http://platform";
            }

            public Role getOcpiRole() {
                return Role.EMSP;
            }

            public VersionNumber getOcpiVersion() {
                return VersionNumber.V_2_2_1;
            }

            public boolean isToLogCurlCommands() {
                return false;
            }
        };
        client = new PlatformClient(restTemplate, config, Validation.buildDefaultValidatorFactory().getValidator());
    }

    private OcpiResponse<String> send() {
        return client.sendOutflowRequest("DE_OLI", "DE", "OLI", InterfaceRole.RECEIVER, ModuleID.TOKENS,
                HttpMethod.GET, null, String.class, List.of("DE", "OLI", "T1"));
    }

    private void respond(HttpStatus status, String body) {
        server.expect(requestTo(URL)).andRespond(withStatus(status).contentType(MediaType.APPLICATION_JSON).body(body));
    }

    @Test
    void success_returnsTheResponse() {
        server.expect(requestTo(URL)).andRespond(
                withSuccess("{\"data\":\"ok\",\"status_code\":1000,\"timestamp\":\"2026-10-27T10:00:00Z\"}",
                        MediaType.APPLICATION_JSON));

        OcpiResponse<String> response = send();

        assertEquals(1000, response.getStatus_code());
        assertEquals("ok", response.getData());
    }

    @Test
    void ocpiErrorWithHttp200_throwsWithTheUpstreamStatus() {
        respond(HttpStatus.OK, "{\"status_code\":3000,\"status_message\":\"Failed to forward tokens to 1 of 1 role API(s)\","
                + "\"timestamp\":\"2026-10-27T10:00:00Z\"}");

        OcpiUpstreamException ex = assertThrows(OcpiUpstreamException.class, this::send);

        assertEquals(3000, ex.getStatusCode());
        assertEquals(200, ex.getHttpStatus());
        assertTrue(ex.getErrorMessage().contains("GET tokens to DE/OLI"), ex.getErrorMessage());
        assertTrue(ex.getErrorMessage().contains("Failed to forward tokens to 1 of 1 role API(s)"), ex.getErrorMessage());
    }

    @Test
    void clientErrorStatus_isKept() {
        respond(HttpStatus.OK, "{\"status_code\":2001,\"status_message\":\"Missing uid\",\"timestamp\":\"2026-10-27T10:00:00Z\"}");

        OcpiUpstreamException ex = assertThrows(OcpiUpstreamException.class, this::send);

        assertEquals(2001, ex.getStatusCode());
    }

    @Test
    void non2xxWithOcpiBody_keepsTheBodyStatus() {
        respond(HttpStatus.INTERNAL_SERVER_ERROR, "{\"status_code\":3001,\"status_message\":\"Unable to use the client's API\"}");

        OcpiUpstreamException ex = assertThrows(OcpiUpstreamException.class, this::send);

        assertEquals(3001, ex.getStatusCode());
        assertEquals(500, ex.getHttpStatus());
        assertTrue(ex.getErrorMessage().contains("Unable to use the client's API"), ex.getErrorMessage());
    }

    @Test
    void serverErrorWithoutOcpiBody_isAServerError() {
        server.expect(requestTo(URL)).andRespond(withStatus(HttpStatus.BAD_GATEWAY).body("upstream connect error"));

        OcpiUpstreamException ex = assertThrows(OcpiUpstreamException.class, this::send);

        assertEquals(3000, ex.getStatusCode());
        assertTrue(ex.getErrorMessage().contains("HTTP 502"), ex.getErrorMessage());
        assertTrue(ex.getErrorMessage().contains("upstream connect error"), ex.getErrorMessage());
    }

    @Test
    void clientErrorWithoutOcpiBody_isAServerError() {
        server.expect(requestTo(URL)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        OcpiUpstreamException ex = assertThrows(OcpiUpstreamException.class, this::send);

        assertEquals(3000, ex.getStatusCode());
        assertEquals(404, ex.getHttpStatus());
    }

    @Test
    void bodyWithoutStatusCode_isAServerError() {
        respond(HttpStatus.OK, "{\"data\":\"ok\"}");

        OcpiUpstreamException ex = assertThrows(OcpiUpstreamException.class, this::send);

        assertEquals(3000, ex.getStatusCode());
    }

    @Test
    void transportFailure_keepsTheCause() {
        RestTemplate restTemplate = new RestTemplate();
        PlatformConfiguration config = new PlatformConfiguration() {
            public String getPlatformUrl() {
                return "http://127.0.0.1:1";
            }

            public Role getOcpiRole() {
                return Role.EMSP;
            }

            public VersionNumber getOcpiVersion() {
                return VersionNumber.V_2_2_1;
            }

            public boolean isToLogCurlCommands() {
                return false;
            }
        };
        PlatformClient unreachable = new PlatformClient(restTemplate, config,
                Validation.buildDefaultValidatorFactory().getValidator());

        OCPICustomException ex = assertThrows(OCPICustomException.class,
                () -> unreachable.sendOutflowRequest("DE_OLI", "DE", "OLI", InterfaceRole.RECEIVER, ModuleID.TOKENS,
                        HttpMethod.GET, null, String.class, List.of("T1")));

        assertEquals(3000, ex.getStatusCode());
        assertTrue(ex.getErrorMessage().startsWith("GET tokens to DE/OLI failed: "), ex.getErrorMessage());
    }
}
