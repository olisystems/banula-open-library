package com.banula.openlib.ocpi.custom.smartlocations.util;

import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.custom.smartlocations.SmartLocationState;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmartLocationActivationUtilTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 8, 24);

    @Test
    void today_shouldFallBackToBerlin_whenZoneIdIsBlankOrUnknown() {
        LocalDate berlin = LocalDate.now(java.time.ZoneId.of("Europe/Berlin"));

        assertEquals(berlin, SmartLocationActivationUtil.today(null));
        assertEquals(berlin, SmartLocationActivationUtil.today("  "));
        assertEquals(berlin, SmartLocationActivationUtil.today("Not/AZone"));
    }

    @Test
    void today_shouldUseGivenZone_whenZoneIdIsValid() {
        assertEquals(LocalDate.now(java.time.ZoneId.of("UTC")), SmartLocationActivationUtil.today("UTC"));
    }

    @Test
    void hasActivationWindow_shouldRequireBothDays() {
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(window(null, null)));
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(window(TODAY, null)));
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(window(null, TODAY)));
        assertTrue(SmartLocationActivationUtil.hasActivationWindow(window(TODAY, TODAY)));
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(null));
    }

    @Test
    void isWindowValid_shouldRejectInvertedWindow() {
        assertTrue(SmartLocationActivationUtil.isWindowValid(window(TODAY.minusDays(1), TODAY)));
        assertTrue(SmartLocationActivationUtil.isWindowValid(window(TODAY, TODAY)));
        assertFalse(SmartLocationActivationUtil.isWindowValid(window(TODAY, TODAY.minusDays(1))));
    }

    @Test
    void isWithinActiveWindow_shouldIncludeBothEdgeDays() {
        SmartLocation location = window(TODAY.minusDays(1), TODAY.plusDays(1));

        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(location, TODAY.minusDays(1)));
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(location, TODAY));
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(location, TODAY.plusDays(1)));
    }

    @Test
    void isWithinActiveWindow_shouldExcludeDayBeforeAndDayAfter() {
        SmartLocation location = window(TODAY.minusDays(1), TODAY.plusDays(1));

        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(location, TODAY.minusDays(2)));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(location, TODAY.plusDays(2)));
    }

    @Test
    void isWithinActiveWindow_shouldAcceptOneDayWindow() {
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(window(TODAY, TODAY), TODAY));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(window(TODAY, TODAY), TODAY.plusDays(1)));
    }

    @Test
    void isWithinActiveWindow_shouldReturnFalse_whenOnlyOneDayIsSet() {
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(window(TODAY, null), TODAY));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(window(null, TODAY), TODAY));
    }

    @Test
    void isWithinActiveWindow_shouldReturnFalse_whenWindowIsInverted() {
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(window(TODAY.plusDays(1), TODAY.minusDays(1)),
                TODAY));
    }

    @Test
    void isWithinActiveWindow_shouldReturnFalse_whenTodayIsNull() {
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(window(TODAY, TODAY), null));
    }

    @Test
    void resolveState_shouldPromoteVerifiedInsideWindow() {
        SmartLocation location = window(TODAY, TODAY, SmartLocationState.VERIFIED);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldKeepVerifiedOutsideWindow() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.minusDays(5), SmartLocationState.VERIFIED);

        assertEquals(SmartLocationState.VERIFIED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldKeepActiveInsideWindow() {
        SmartLocation location = window(TODAY.minusDays(1), TODAY.plusDays(1), SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldDemoteActiveOutsideWindow() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.minusDays(5), SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.VERIFIED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldDemoteActive_whenWindowIsRemoved() {
        SmartLocation location = window(null, null, SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.VERIFIED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldNeverTouchManualStates() {
        List<SmartLocationState> manualStates = List.of(SmartLocationState.PLAIN_OCPI, SmartLocationState.ENRICHED,
                SmartLocationState.INVALID, SmartLocationState.ARCHIVED);

        for (SmartLocationState state : manualStates) {
            assertEquals(state, SmartLocationActivationUtil.resolveState(window(TODAY, TODAY, state), TODAY),
                    "in-window " + state);
            assertEquals(state,
                    SmartLocationActivationUtil.resolveState(window(TODAY.minusDays(9), TODAY.minusDays(8), state),
                            TODAY),
                    "out-of-window " + state);
        }
    }

    @Test
    void resolveState_shouldKeepNullState() {
        assertNull(SmartLocationActivationUtil.resolveState(window(TODAY, TODAY, null), TODAY));
        assertNull(SmartLocationActivationUtil.resolveState(null, TODAY));
    }

    @Test
    void applyActiveState_shouldReportChange_onlyOnTheFirstRun() {
        SmartLocation location = window(TODAY, TODAY, SmartLocationState.VERIFIED);

        assertTrue(SmartLocationActivationUtil.applyActiveState(location, TODAY));
        assertEquals(SmartLocationState.ACTIVE, location.getSmartLocationState());

        assertFalse(SmartLocationActivationUtil.applyActiveState(location, TODAY));
        assertEquals(SmartLocationState.ACTIVE, location.getSmartLocationState());
    }

    @Test
    void applyActiveState_shouldBeReversible() {
        SmartLocation location = window(TODAY, TODAY, SmartLocationState.VERIFIED);

        assertTrue(SmartLocationActivationUtil.applyActiveState(location, TODAY));
        assertTrue(SmartLocationActivationUtil.applyActiveState(location, TODAY.plusDays(1)));
        assertEquals(SmartLocationState.VERIFIED, location.getSmartLocationState());
        assertFalse(SmartLocationActivationUtil.applyActiveState(location, TODAY.plusDays(1)));
    }

    @Test
    void applyActiveState_shouldReturnFalse_whenLocationIsNull() {
        assertFalse(SmartLocationActivationUtil.applyActiveState(null, TODAY));
    }

    @Test
    void isPubliclyServable_shouldOnlyAcceptActive() {
        assertTrue(SmartLocationActivationUtil.isPubliclyServable(SmartLocationState.ACTIVE));
        assertFalse(SmartLocationActivationUtil.isPubliclyServable(SmartLocationState.VERIFIED));
        assertFalse(SmartLocationActivationUtil.isPubliclyServable(null));
        for (SmartLocationState state : SmartLocationState.values()) {
            assertEquals(state == SmartLocationState.ACTIVE, SmartLocationActivationUtil.isPubliclyServable(state));
        }
        assertNotNull(SmartLocationState.ACTIVE);
    }

    private SmartLocation window(LocalDate first, LocalDate last) {
        return window(first, last, SmartLocationState.VERIFIED);
    }

    private SmartLocation window(LocalDate first, LocalDate last, SmartLocationState state) {
        SmartLocation location = new SmartLocation();
        location.setCountryCode("DE");
        location.setPartyId("ABC");
        location.setId("LOCTEST");
        location.setSmartLocationState(state);
        location.setActiveFirstDay(first);
        location.setActiveLastDay(last);
        return location;
    }
}
