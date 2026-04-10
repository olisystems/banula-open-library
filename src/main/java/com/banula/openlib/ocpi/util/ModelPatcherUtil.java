package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.model.ChargingSession;
import com.banula.openlib.ocpi.model.Location;
import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.vo.Connector;
import com.banula.openlib.ocpi.model.vo.EVSE;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/*
  This model patcher methods iterates through every field of the incomplete dto request and uses the filled-in fields
  to update the existing record.
 */

public class ModelPatcherUtil {

    private static boolean patchObjectFields(Object existingObject, Object incompleteObject, Class<?> type)
            throws IllegalAccessException {
        if (type == null || type == Object.class) {
            return false;
        }

        boolean changed = false;
        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || field.getName().equals("lastUpdated")) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(incompleteObject);
            if (value != null) {
                Object existingValue = field.get(existingObject);
                if (!Objects.equals(value, existingValue)) {
                    field.set(existingObject, value);
                    changed = true;
                }
            }
            field.setAccessible(false);
        }

        return changed | patchObjectFields(existingObject, incompleteObject, type.getSuperclass());
    }

    public static void tokenPatcher(Token existingToken, Token incompleteToken) throws IllegalAccessException {
        if (patchObjectFields(existingToken, incompleteToken, Token.class)) {
            existingToken.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        }
    }

    public static void locationPatcher(Location existingLocation, Location incompleteLocation)
            throws IllegalAccessException {
        if (patchObjectFields(existingLocation, incompleteLocation, Location.class)) {
            existingLocation.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        }
    }

    public static void evsePatcher(EVSE existingEvse, EVSE incompleteEvse) throws IllegalAccessException {
        evsePatcher(null, existingEvse, incompleteEvse);
    }

    public static void evsePatcher(Location location, EVSE existingEvse, EVSE incompleteEvse)
            throws IllegalAccessException {
        if (patchObjectFields(existingEvse, incompleteEvse, EVSE.class)) {
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            existingEvse.setLastUpdated(now);
            if (location != null) {
                location.setLastUpdated(now);
            }
        }
    }

    public static void connectorPatcher(Connector existingConnector, Connector incompleteConnector)
            throws IllegalAccessException {
        connectorPatcher(null, null, existingConnector, incompleteConnector);
    }

    public static void connectorPatcher(Location location, EVSE evse, Connector existingConnector,
            Connector incompleteConnector) throws IllegalAccessException {
        if (patchObjectFields(existingConnector, incompleteConnector, Connector.class)) {
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            existingConnector.setLastUpdated(now);
            if (evse != null) {
                evse.setLastUpdated(now);
            }
            if (location != null) {
                location.setLastUpdated(now);
            }
        }
    }

    public static void sessionPatcher(ChargingSession existingSession, ChargingSession incompleteSession)
            throws IllegalAccessException {
        if (patchObjectFields(existingSession, incompleteSession, ChargingSession.class)) {
            existingSession.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        }
    }

    public static void smartLocationPatcher(SmartLocation existingLocation, SmartLocation incompleteLocation)
            throws IllegalAccessException {
        if (patchObjectFields(existingLocation, incompleteLocation, SmartLocation.class)) {
            existingLocation.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        }
    }

}
