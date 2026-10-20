package com.banula.openlib.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

class TraceContextTest {

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void outgoingPropagatesInboundCorrelationIdWithFreshRequestId() {
        MDC.put(TraceContext.MDC_CORRELATION_ID, "inbound-cid");
        MDC.put(TraceContext.MDC_REQUEST_ID, "inbound-rid");

        try (TraceContext.Outgoing first = TraceContext.outgoing();
                TraceContext.Outgoing second = TraceContext.outgoing()) {
            assertEquals("inbound-cid", first.correlationId());
            assertEquals("inbound-cid", second.correlationId());
            assertNotEquals("inbound-rid", first.requestId());
            assertNotEquals(first.requestId(), second.requestId());
        }
        assertEquals("inbound-cid", MDC.get(TraceContext.MDC_CORRELATION_ID));
    }

    @Test
    void outgoingWithoutInboundCorrelationIdGeneratesOneScopedToTheCall() {
        String correlationId;
        try (TraceContext.Outgoing trace = TraceContext.outgoing()) {
            correlationId = trace.correlationId();
            assertNotNull(correlationId);
            assertEquals(correlationId, MDC.get(TraceContext.MDC_CORRELATION_ID));
        }
        assertNull(MDC.get(TraceContext.MDC_CORRELATION_ID));

        try (TraceContext.Outgoing next = TraceContext.outgoing()) {
            assertNotEquals(correlationId, next.correlationId());
        }
    }

    @Test
    void applyToSetsBothHeaders() {
        Map<String, String> headers = new HashMap<>();
        try (TraceContext.Outgoing trace = TraceContext.outgoing()) {
            trace.applyTo(headers::put);
            assertEquals(trace.correlationId(), headers.get(TraceContext.CORRELATION_ID_HEADER));
            assertEquals(trace.requestId(), headers.get(TraceContext.REQUEST_ID_HEADER));
        }
    }

    @Test
    void partyRequiresBothParts() {
        assertEquals("DE/ABC", TraceContext.party("DE", "ABC"));
        assertNull(TraceContext.party("DE", null));
        assertNull(TraceContext.party(" ", "ABC"));
    }
}
