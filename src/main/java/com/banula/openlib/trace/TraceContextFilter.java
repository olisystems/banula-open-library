package com.banula.openlib.trace;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String correlationId = TraceContext.valueOrNewId(request.getHeader(TraceContext.CORRELATION_ID_HEADER));
        String requestId = TraceContext.valueOrNewId(request.getHeader(TraceContext.REQUEST_ID_HEADER));
        String from = TraceContext.party(
                request.getHeader("OCPI-from-country-code"), request.getHeader("OCPI-from-party-id"));
        String to = TraceContext.party(
                request.getHeader("OCPI-to-country-code"), request.getHeader("OCPI-to-party-id"));
        MDC.put(TraceContext.MDC_CORRELATION_ID, correlationId);
        MDC.put(TraceContext.MDC_REQUEST_ID, requestId);
        putIfPresent(TraceContext.MDC_OCPI_FROM, from);
        putIfPresent(TraceContext.MDC_OCPI_TO, to);
        TraceContext.setSpanAttribute(TraceContext.SPAN_CORRELATION_ID, correlationId);
        TraceContext.setSpanAttribute(TraceContext.SPAN_REQUEST_ID, requestId);
        TraceContext.setSpanAttribute(TraceContext.SPAN_OCPI_FROM, from);
        TraceContext.setSpanAttribute(TraceContext.SPAN_OCPI_TO, to);
        response.setHeader(TraceContext.CORRELATION_ID_HEADER, correlationId);
        response.setHeader(TraceContext.REQUEST_ID_HEADER, requestId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceContext.MDC_CORRELATION_ID);
            MDC.remove(TraceContext.MDC_REQUEST_ID);
            MDC.remove(TraceContext.MDC_OCPI_FROM);
            MDC.remove(TraceContext.MDC_OCPI_TO);
        }
    }

    private static void putIfPresent(String key, String value) {
        if (value != null) {
            MDC.put(key, value);
        }
    }
}
