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
    void hasActivationWindow_shouldRequireOnlyTheFirstDay() {
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(window(null, null)));
        assertTrue(SmartLocationActivationUtil.hasActivationWindow(window(TODAY, null)));
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(window(null, TODAY)));
        assertTrue(SmartLocationActivationUtil.hasActivationWindow(window(TODAY, TODAY)));
        assertFalse(SmartLocationActivationUtil.hasActivationWindow(null));
    }

    @Test
    void isWindowValid_shouldRejectInvertedWindow() {
        assertTrue(SmartLocationActivationUtil.isWindowValid(window(TODAY.minusDays(1), TODAY)));
        assertTrue(SmartLocationActivationUtil.isWindowValid(window(TODAY, TODAY)));
        assertTrue(SmartLocationActivationUtil.isWindowValid(window(TODAY, null)));
        assertFalse(SmartLocationActivationUtil.isWindowValid(window(null, TODAY)));
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
    void isWithinActiveWindow_shouldNeverEnd_whenLastDayIsBlank() {
        SmartLocation openEnded = window(TODAY, null);

        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(openEnded, TODAY));
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(openEnded, TODAY.plusYears(5)));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(openEnded, TODAY.minusDays(1)));
    }

    @Test
    void isWithinActiveWindow_shouldReturnFalse_whenOnlyTheLastDayIsSet() {
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(window(null, TODAY), TODAY));
    }

    @Test
    void isWithinActiveWindow_dateOnlyForm_shouldMatchTheEntityForm() {
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(TODAY.minusDays(1), TODAY.plusDays(1), TODAY));
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(TODAY, null, TODAY));
        assertTrue(SmartLocationActivationUtil.isWithinActiveWindow(TODAY.minusDays(30), null, TODAY));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(TODAY.plusDays(1), null, TODAY));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(null, TODAY, TODAY));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(TODAY, TODAY, null));
        assertFalse(SmartLocationActivationUtil.isWithinActiveWindow(TODAY.minusDays(10), TODAY.minusDays(5), TODAY));
    }

    @Test
    void isWindowPassed_shouldOnlyBeTrue_forAClosedWindowThatEnded() {
        assertTrue(SmartLocationActivationUtil.isWindowPassed(TODAY.minusDays(10), TODAY.minusDays(1), TODAY));
        assertFalse(SmartLocationActivationUtil.isWindowPassed(TODAY.minusDays(10), TODAY, TODAY));
        assertFalse(SmartLocationActivationUtil.isWindowPassed(TODAY.minusDays(10), null, TODAY));
        assertFalse(SmartLocationActivationUtil.isWindowPassed(null, TODAY.minusDays(1), TODAY));
        assertFalse(SmartLocationActivationUtil.isWindowPassed(TODAY.minusDays(10), TODAY.minusDays(1), null));
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
    void resolveState_shouldKeepVerified_whenWindowHasNotStartedYet() {
        SmartLocation location = window(TODAY.plusDays(5), TODAY.plusDays(10), SmartLocationState.VERIFIED);

        assertEquals(SmartLocationState.VERIFIED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldKeepActiveInsideWindow() {
        SmartLocation location = window(TODAY.minusDays(1), TODAY.plusDays(1), SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldArchiveActive_whenBothDaysAreInThePast() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.minusDays(5), SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.ARCHIVED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldArchiveVerified_whenBothDaysAreInThePast() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.minusDays(5), SmartLocationState.VERIFIED);

        assertEquals(SmartLocationState.ARCHIVED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldArchiveOnTheDayAfterTheWindowEnds() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY, SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
        assertEquals(SmartLocationState.ARCHIVED,
                SmartLocationActivationUtil.resolveState(location, TODAY.plusDays(1)));
    }

    @Test
    void resolveState_shouldActivateOpenEndedWindow_fromItsFirstDayOnwards() {
        SmartLocation location = window(TODAY, null, SmartLocationState.VERIFIED);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
        assertEquals(SmartLocationState.ACTIVE,
                SmartLocationActivationUtil.resolveState(location, TODAY.plusYears(3)));
    }

    @Test
    void resolveState_shouldKeepVerified_whenOpenEndedWindowHasNotStarted() {
        SmartLocation location = window(TODAY.plusDays(1), null, SmartLocationState.VERIFIED);

        assertEquals(SmartLocationState.VERIFIED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    /**
     * The reversibility requirement: a last day entered by mistake archives the
     * location, and clearing it again brings the location straight back to ACTIVE.
     */
    @Test
    void resolveState_shouldReviveArchived_whenTheLastDayIsCleared() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.minusDays(1), SmartLocationState.VERIFIED);
        assertEquals(SmartLocationState.ARCHIVED, SmartLocationActivationUtil.resolveState(location, TODAY));

        location.setSmartLocationState(SmartLocationState.ARCHIVED);
        location.setActiveLastDay(null);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldReviveArchived_whenTheWindowIsExtendedOverToday() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.plusDays(5), SmartLocationState.ARCHIVED);

        assertEquals(SmartLocationState.ACTIVE, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldKeepArchived_whenItHasNoWindow() {
        SmartLocation location = window(null, null, SmartLocationState.ARCHIVED);

        assertEquals(SmartLocationState.ARCHIVED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldKeepArchived_whenTheWindowIsStillInThePast() {
        SmartLocation location = window(TODAY.minusDays(10), TODAY.minusDays(5), SmartLocationState.ARCHIVED);

        assertEquals(SmartLocationState.ARCHIVED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldNeverMove_whenTheWindowIsInverted() {
        for (SmartLocationState state : List.of(SmartLocationState.VERIFIED, SmartLocationState.ACTIVE,
                SmartLocationState.ARCHIVED)) {
            assertEquals(state,
                    SmartLocationActivationUtil.resolveState(
                            window(TODAY.plusDays(1), TODAY.minusDays(1), state), TODAY),
                    "inverted " + state);
        }
    }

    @Test
    void resolveState_shouldDemoteActive_whenWindowIsRemoved() {
        SmartLocation location = window(null, null, SmartLocationState.ACTIVE);

        assertEquals(SmartLocationState.VERIFIED, SmartLocationActivationUtil.resolveState(location, TODAY));
    }

    @Test
    void resolveState_shouldNeverTouchManualStates() {
        List<SmartLocationState> manualStates = List.of(SmartLocationState.PLAIN_OCPI, SmartLocationState.ENRICHED,
                SmartLocationState.INVALID);

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
        assertEquals(SmartLocationState.ARCHIVED, location.getSmartLocationState());
        assertFalse(SmartLocationActivationUtil.applyActiveState(location, TODAY.plusDays(1)));

        // Clearing the mistaken last day brings it straight back.
        location.setActiveLastDay(null);
        assertTrue(SmartLocationActivationUtil.applyActiveState(location, TODAY.plusDays(1)));
        assertEquals(SmartLocationState.ACTIVE, location.getSmartLocationState());
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
