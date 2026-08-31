package com.banula.openlib.ocpi.custom.smartlocations.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;

import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.custom.smartlocations.SmartLocationState;

import lombok.extern.slf4j.Slf4j;

/**
 * Decides whether a {@link SmartLocation} is currently inside its activation
 * window, and which {@link SmartLocationState} follows from that.
 *
 * <p>
 * This is the single implementation shared by every service that evaluates
 * activation windows (banula-nsp and banula-cdr-adapter today). Both must reach
 * bit-identical answers, otherwise the cdr-adapter mirror desynchronises from
 * the NSP source of truth.
 *
 * <p>
 * A window needs only a <b>first</b> day. Leaving the last day blank opens the
 * window ended, so the location stays active from its first day onwards; setting
 * a last day that has already passed archives the location, and clearing that
 * last day again brings it back — the transition is fully reversible.
 *
 * <p>
 * The evaluation is <b>idempotent</b> (running it twice changes nothing) and
 * <b>reversible</b> (a window moved into the past demotes the location again),
 * which is what makes an on-demand refresh endpoint safe.
 */
@Slf4j
public class SmartLocationActivationUtil {

    /** Fallback zone: activation windows are defined in German calendar days. */
    public static final String DEFAULT_ZONE_ID = "Europe/Berlin";

    private SmartLocationActivationUtil() {
    }

    /**
     * Today's calendar day in the given zone, falling back to
     * {@link #DEFAULT_ZONE_ID} when the zone is blank or unknown.
     */
    public static LocalDate today(String zoneId) {
        return LocalDate.now(resolveZone(zoneId));
    }

    private static ZoneId resolveZone(String zoneId) {
        if (zoneId == null || zoneId.isBlank()) {
            return ZoneId.of(DEFAULT_ZONE_ID);
        }
        try {
            return ZoneId.of(zoneId.trim());
        } catch (DateTimeException e) {
            log.warn("Unknown zone id '{}' for smart location activation, falling back to {}", zoneId,
                    DEFAULT_ZONE_ID);
            return ZoneId.of(DEFAULT_ZONE_ID);
        }
    }

    /**
     * A location has an activation window as soon as its <b>first</b> day is set.
     * The last day is optional: without it the window never ends.
     */
    public static boolean hasActivationWindow(SmartLocation location) {
        return location != null && location.getActiveFirstDay() != null;
    }

    /** A window is valid when its first day is not after its last day. */
    public static boolean isWindowValid(SmartLocation location) {
        return location != null && isWindowValid(location.getActiveFirstDay(), location.getActiveLastDay());
    }

    /**
     * Date-only form of {@link #isWindowValid(SmartLocation)}, for callers that
     * hold the two days without the entity (e.g. a DTO).
     */
    public static boolean isWindowValid(LocalDate activeFirstDay, LocalDate activeLastDay) {
        return activeFirstDay != null
                && (activeLastDay == null || !activeFirstDay.isAfter(activeLastDay));
    }

    /**
     * Whether {@code today} falls inside the location's activation window, with
     * <b>both edge days inclusive</b>. An open-ended window (no last day) covers
     * every day from the first day onwards. An inverted window never activates and
     * is logged so that bad data stays visible.
     */
    public static boolean isWithinActiveWindow(SmartLocation location, LocalDate today) {
        if (location == null) {
            return false;
        }
        return isWithinActiveWindow(location.getActiveFirstDay(), location.getActiveLastDay(), today,
                location.getId());
    }

    /**
     * Date-only form of {@link #isWithinActiveWindow(SmartLocation, LocalDate)}, so
     * callers holding a DTO — or any pair of days — get exactly the same answer as
     * the entity-based evaluation.
     */
    public static boolean isWithinActiveWindow(LocalDate activeFirstDay, LocalDate activeLastDay,
            LocalDate reference) {
        return isWithinActiveWindow(activeFirstDay, activeLastDay, reference, null);
    }

    private static boolean isWithinActiveWindow(LocalDate activeFirstDay, LocalDate activeLastDay,
            LocalDate reference, String locationId) {
        if (activeFirstDay == null || reference == null) {
            return false;
        }
        if (!isWindowValid(activeFirstDay, activeLastDay)) {
            log.warn("Smart location {} has an inverted activation window ({} > {}); it will never activate",
                    locationId, activeFirstDay, activeLastDay);
            return false;
        }
        if (reference.isBefore(activeFirstDay)) {
            return false;
        }
        return activeLastDay == null || !reference.isAfter(activeLastDay);
    }

    /**
     * Whether the window has already ended — i.e. both days are set and
     * {@code reference} is past the last one. An open-ended window never passes.
     */
    public static boolean isWindowPassed(LocalDate activeFirstDay, LocalDate activeLastDay, LocalDate reference) {
        return activeFirstDay != null && activeLastDay != null && reference != null
                && reference.isAfter(activeLastDay);
    }

    /**
     * The state the location should have today.
     *
     * <p>
     * Only {@code VERIFIED}, {@code ACTIVE} and {@code ARCHIVED} ever move, and
     * only into each other: a window covering today activates, a window that has
     * passed archives, and a window still in the future waits as {@code VERIFIED}.
     * {@code PLAIN_OCPI}, {@code ENRICHED} and {@code INVALID} are manual decisions
     * and always win. On a location with no window at all every state is left as it
     * is — which is what keeps a hand-picked {@code ARCHIVED} sticky — except
     * {@code ACTIVE}, which is only ever derived from a window and therefore falls
     * back to {@code VERIFIED}.
     */
    public static SmartLocationState resolveState(SmartLocation location, LocalDate today) {
        if (location == null) {
            return null;
        }

        SmartLocationState current = location.getSmartLocationState();
        if (current != SmartLocationState.VERIFIED && current != SmartLocationState.ACTIVE
                && current != SmartLocationState.ARCHIVED) {
            return current;
        }

        // No window at all: a hand-picked ARCHIVED stays sticky, but ACTIVE cannot
        // survive on its own — it is only ever derived from a window.
        if (!hasActivationWindow(location)) {
            return current == SmartLocationState.ACTIVE ? SmartLocationState.VERIFIED : current;
        }

        if (isWithinActiveWindow(location, today)) {
            return SmartLocationState.ACTIVE;
        }

        // Inverted windows are bad data, not a decision: never move on them.
        if (!isWindowValid(location)) {
            return current;
        }

        if (isWindowPassed(location.getActiveFirstDay(), location.getActiveLastDay(), today)) {
            return SmartLocationState.ARCHIVED;
        }

        return SmartLocationState.VERIFIED;
    }

    /**
     * Applies {@link #resolveState(SmartLocation, LocalDate)} to the location.
     *
     * @return {@code true} only when the state actually changed, so callers can
     *         skip the write — and therefore avoid bumping {@code lastUpdated},
     *         the cursor OCPI clients page on.
     */
    public static boolean applyActiveState(SmartLocation location, LocalDate today) {
        if (location == null) {
            return false;
        }

        SmartLocationState resolved = resolveState(location, today);
        if (resolved == location.getSmartLocationState()) {
            return false;
        }

        log.info("Smart location {} moves from {} to {} (window {} .. {}, today {})", location.getId(),
                location.getSmartLocationState(), resolved, location.getActiveFirstDay(),
                location.getActiveLastDay(), today);
        location.setSmartLocationState(resolved);
        return true;
    }

    /**
     * The single answer to "may this location be served to other parties?" — used
     * by the {@code publish} flag and by every sender endpoint, so a future policy
     * change is one edit.
     */
    public static boolean isPubliclyServable(SmartLocationState state) {
        return state == SmartLocationState.ACTIVE;
    }

}
