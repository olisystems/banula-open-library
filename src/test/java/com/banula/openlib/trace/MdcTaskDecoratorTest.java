package com.banula.openlib.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

class MdcTaskDecoratorTest {

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void copiesSubmittingThreadMdcIntoTask() throws Exception {
        MDC.put(TraceContext.MDC_CORRELATION_ID, "cid-async");
        AtomicReference<String> seen = new AtomicReference<>();
        Runnable decorated = new MdcTaskDecorator()
                .decorate(() -> seen.set(MDC.get(TraceContext.MDC_CORRELATION_ID)));
        MDC.clear();

        CompletableFuture.runAsync(decorated).get();

        assertEquals("cid-async", seen.get());
    }

    @Test
    void restoresCallerMdcWhenRunOnCallerThread() {
        Runnable decorated = new MdcTaskDecorator().decorate(() -> {
        });
        MDC.put(TraceContext.MDC_CORRELATION_ID, "caller-cid");

        decorated.run();

        assertEquals("caller-cid", MDC.get(TraceContext.MDC_CORRELATION_ID));
        MDC.clear();
        decorated.run();
        assertNull(MDC.get(TraceContext.MDC_CORRELATION_ID));
    }
}
