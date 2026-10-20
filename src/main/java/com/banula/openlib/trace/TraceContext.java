package com.banula.openlib.trace;

import java.util.UUID;
import java.util.function.BiConsumer;

import org.slf4j.MDC;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;

import lombok.extern.slf4j.Slf4j;

/**
 * Workspace-wide trace convention shared with the OCN node and the Go relays: the
 * X-Correlation-ID of an inbound request is propagated to every outgoing OCPI call made
 * while handling it, while each outgoing call gets its own fresh X-Request-ID.
 */
@Slf4j
public final class TraceContext {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    public static final String MDC_CORRELATION_ID = "correlation_id";
    public static final String MDC_REQUEST_ID = "request_id";
    public static final String MDC_OCPI_FROM = "ocpi_from";
    public static final String MDC_OCPI_TO = "ocpi_to";

    public static final String SPAN_CORRELATION_ID = "ocpi.correlation_id";
    public static final String SPAN_REQUEST_ID = "ocpi.request_id";
    public static final String SPAN_OUT_REQUEST_ID = "ocpi.out_request_id";
    public static final String SPAN_OCPI_FROM = "ocpi.from";
    public static final String SPAN_OCPI_TO = "ocpi.to";
    public static final String SPAN_FORWARD_EVENT = "ocpi.forward";
    public static final String SPAN_URL_FULL = "url.full";

    private TraceContext() {
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public static String valueOrNewId(String value) {
        return isBlank(value) ? newId() : value;
    }

    /** "CC/PID", or null unless both parts are present. */
    public static String party(String countryCode, String partyId) {
        return isBlank(countryCode) || isBlank(partyId) ? null : countryCode + "/" + partyId;
    }

    /**
     * Opens the trace for one outgoing call. Without an inbound correlation id (scheduled
     * jobs, async threads, internal triggers) a fresh one is generated and kept in MDC until
     * {@link Outgoing#close()}, so log lines of that call carry it.
     */
    public static Outgoing outgoing() {
        String correlationId = MDC.get(MDC_CORRELATION_ID);
        boolean generated = isBlank(correlationId);
        if (generated) {
            correlationId = newId();
            MDC.put(MDC_CORRELATION_ID, correlationId);
        }
        return new Outgoing(correlationId, newId(), generated);
    }

    /** Sets {@code value} on the active OpenTelemetry span (the agent's SERVER span inside a request). */
    static void setSpanAttribute(String key, String value) {
        if (!isBlank(value)) {
            Span.current().setAttribute(key, value);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static final class Outgoing implements AutoCloseable {
        private final String correlationId;
        private final String requestId;
        private final boolean ownsMdcCorrelationId;

        private Outgoing(String correlationId, String requestId, boolean ownsMdcCorrelationId) {
            this.correlationId = correlationId;
            this.requestId = requestId;
            this.ownsMdcCorrelationId = ownsMdcCorrelationId;
        }

        public String correlationId() {
            return correlationId;
        }

        public String requestId() {
            return requestId;
        }

        public void applyTo(BiConsumer<String, String> headerSetter) {
            headerSetter.accept(REQUEST_ID_HEADER, requestId);
            headerSetter.accept(CORRELATION_ID_HEADER, correlationId);
        }

        public void log(Object method, String url) {
            log.info("OCPI out | {} {} | correlation_id={} request_id={}", method, url, correlationId, requestId);
            // The agent's RestTemplate CLIENT span only exists inside exchange(), so the IDs go on
            // the span that triggered the call.
            Span.current().addEvent(SPAN_FORWARD_EVENT, Attributes.builder()
                    .put(SPAN_CORRELATION_ID, correlationId)
                    .put(SPAN_OUT_REQUEST_ID, requestId)
                    .put(SPAN_URL_FULL, url)
                    .build());
        }

        @Override
        public void close() {
            if (ownsMdcCorrelationId) {
                MDC.remove(MDC_CORRELATION_ID);
            }
        }
    }
}
