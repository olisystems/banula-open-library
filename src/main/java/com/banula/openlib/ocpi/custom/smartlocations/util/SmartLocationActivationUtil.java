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
     * A location has an activation window only when <b>both</b> edge days are set;
     * a single day on its own does nothing.
     */
    public static boolean hasActivationWindow(SmartLocation location) {
        return location != null && location.getActiveFirstDay() != null && location.getActiveLastDay() != null;
    }

    /** A window is valid when its first day is not after its last day. */
    public static boolean isWindowValid(SmartLocation location) {
        return hasActivationWindow(location)
                && !location.getActiveFirstDay().isAfter(location.getActiveLastDay());
    }

    /**
     * Whether {@code today} falls inside the location's activation window, with
     * <b>both edge days inclusive</b>. An inverted window never activates and is
     * logged so that bad data stays visible.
     */
    public static boolean isWithinActiveWindow(SmartLocation location, LocalDate today) {
        if (!hasActivationWindow(location) || today == null) {
            return false;
        }
        if (!isWindowValid(location)) {
            log.warn("Smart location {} has an inverted activation window ({} > {}); it will never activate",
                    location.getId(), location.getActiveFirstDay(), location.getActiveLastDay());
            return false;
        }
        return !today.isBefore(location.getActiveFirstDay()) && !today.isAfter(location.getActiveLastDay());
    }

    /**
     * The state the location should have today.
     *
     * <p>
     * Only {@code VERIFIED} and {@code ACTIVE} ever move, and only into each
     * other. {@code PLAIN_OCPI}, {@code ENRICHED}, {@code INVALID} and
     * {@code ARCHIVED} are manual decisions and always win.
     */
    public static SmartLocationState resolveState(SmartLocation location, LocalDate today) {
        if (location == null) {
            return null;
        }

        SmartLocationState current = location.getSmartLocationState();
        if (current != SmartLocationState.VERIFIED && current != SmartLocationState.ACTIVE) {
            return current;
        }

        return isWithinActiveWindow(location, today)
                ? SmartLocationState.ACTIVE
                : SmartLocationState.VERIFIED;
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
