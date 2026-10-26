package com.banula.openlib.ocpi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InfoUtilsMaskTest {

    @Test
    void keepsLastFourOfLongCredentials() {
        assertEquals("Token ****ef12", InfoUtils.maskIfSecret("Authorization", "Token abcdefghijklef12"));
    }

    @Test
    void fullyMasksShortCredentials() {
        assertEquals("Token ****", InfoUtils.maskIfSecret("Authorization", "Token abcdef12"));
        assertEquals("****", InfoUtils.maskIfSecret("authorization", "abcdef12"));
    }

    @Test
    void leavesOtherHeadersAlone() {
        assertEquals("abc", InfoUtils.maskIfSecret("X-Request-ID", "abc"));
    }
}
