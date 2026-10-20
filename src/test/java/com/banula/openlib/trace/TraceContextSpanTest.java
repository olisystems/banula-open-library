package com.banula.openlib.trace;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.testing.junit5.OpenTelemetryExtension;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.SpanData;

class TraceContextSpanTest {

    @RegisterExtension
    static final OpenTelemetryExtension otel = OpenTelemetryExtension.create();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    private SpanData inSpan(ThrowingRunnable block) throws Exception {
        Span span = otel.getOpenTelemetry().getTracer("test").spanBuilder("server").startSpan();
        try (Scope ignored = span.makeCurrent()) {
            block.run();
        } finally {
            span.end();
        }
        return otel.getSpans().get(0);
    }

    @Test
    void filterSetsOcpiAttributesOnCurrentSpan() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ocpi/2.2.1/locations");
        request.addHeader("X-Correlation-ID", "cid-1");
        request.addHeader("X-Request-ID", "rid-1");
        request.addHeader("OCPI-from-country-code", "DE");
        request.addHeader("OCPI-from-party-id", "ABC");

        SpanData span = inSpan(() -> new TraceContextFilter()
                .doFilter(request, new MockHttpServletResponse(), (req, res) -> {
                }));

        assertEquals("cid-1", span.getAttributes().get(stringKey(TraceContext.SPAN_CORRELATION_ID)));
        assertEquals("rid-1", span.getAttributes().get(stringKey(TraceContext.SPAN_REQUEST_ID)));
        assertEquals("DE/ABC", span.getAttributes().get(stringKey(TraceContext.SPAN_OCPI_FROM)));
        assertNull(span.getAttributes().get(stringKey(TraceContext.SPAN_OCPI_TO)));
    }

    @Test
    void outgoingLogAddsForwardEventWithIds() throws Exception {
        MDC.put(TraceContext.MDC_CORRELATION_ID, "cid-1");
        String[] requestId = new String[1];

        SpanData span = inSpan(() -> {
            try (TraceContext.Outgoing trace = TraceContext.outgoing()) {
                requestId[0] = trace.requestId();
                trace.log("GET", "https://cpo.example/ocpi/2.2.1/locations");
            }
        });

        EventData event = span.getEvents().get(0);
        assertEquals(TraceContext.SPAN_FORWARD_EVENT, event.getName());
        assertEquals("cid-1", event.getAttributes().get(stringKey(TraceContext.SPAN_CORRELATION_ID)));
        assertEquals(requestId[0], event.getAttributes().get(stringKey(TraceContext.SPAN_OUT_REQUEST_ID)));
        assertEquals("https://cpo.example/ocpi/2.2.1/locations",
                event.getAttributes().get(stringKey(TraceContext.SPAN_URL_FULL)));
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
