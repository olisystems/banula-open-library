package com.banula.openlib.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

class TraceContextFilterTest {

    private final TraceContextFilter filter = new TraceContextFilter();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void populatesMdcFromInboundHeadersAndEchoesThem() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ocpi/2.2.1/locations");
        request.addHeader("X-Correlation-ID", "cid-1");
        request.addHeader("X-Request-ID", "rid-1");
        request.addHeader("OCPI-from-country-code", "DE");
        request.addHeader("OCPI-from-party-id", "ABC");
        request.addHeader("OCPI-to-country-code", "NL");
        request.addHeader("OCPI-to-party-id", "XYZ");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> seen = new HashMap<>();

        filter.doFilter(request, response, (req, res) -> seen.putAll(MDC.getCopyOfContextMap()));

        assertEquals("cid-1", seen.get(TraceContext.MDC_CORRELATION_ID));
        assertEquals("rid-1", seen.get(TraceContext.MDC_REQUEST_ID));
        assertEquals("DE/ABC", seen.get(TraceContext.MDC_OCPI_FROM));
        assertEquals("NL/XYZ", seen.get(TraceContext.MDC_OCPI_TO));
        assertEquals("cid-1", response.getHeader("X-Correlation-ID"));
        assertEquals("rid-1", response.getHeader("X-Request-ID"));
        assertMdcCleared();
    }

    @Test
    void generatesIdsWhenInboundHeadersAreMissing() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> seen = new HashMap<>();

        filter.doFilter(new MockHttpServletRequest(), response, (req, res) -> seen.putAll(MDC.getCopyOfContextMap()));

        assertNotNull(seen.get(TraceContext.MDC_CORRELATION_ID));
        assertNull(seen.get(TraceContext.MDC_OCPI_FROM));
        assertEquals(seen.get(TraceContext.MDC_CORRELATION_ID), response.getHeader("X-Correlation-ID"));
        assertEquals(seen.get(TraceContext.MDC_REQUEST_ID), response.getHeader("X-Request-ID"));
        assertMdcCleared();
    }

    @Test
    void clearsMdcWhenChainFails() {
        FilterChain failing = (req, res) -> {
            throw new ServletException("boom");
        };

        assertThrows(ServletException.class,
                () -> filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), failing));
        assertMdcCleared();
    }

    private static void assertMdcCleared() {
        assertNull(MDC.get(TraceContext.MDC_CORRELATION_ID));
        assertNull(MDC.get(TraceContext.MDC_REQUEST_ID));
        assertNull(MDC.get(TraceContext.MDC_OCPI_FROM));
        assertNull(MDC.get(TraceContext.MDC_OCPI_TO));
    }
}
