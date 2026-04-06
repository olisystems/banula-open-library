package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.model.ChargingSession;
import com.banula.openlib.ocpi.model.Location;
import com.banula.openlib.ocpi.model.Token;
import com.banula.openlib.ocpi.model.vo.Connector;
import com.banula.openlib.ocpi.model.vo.EVSE;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/*
  This model patcher methods iterates through every field of the incomplete dto request and uses the filled-in fields
  to update the existing record.
 */

public class ModelPatcherUtil {

    private static void patchObjectFields(Object existingObject, Object incompleteObject, Class<?> type)
            throws IllegalAccessException {
        if (type == null || type == Object.class) {
            return;
        }

        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(incompleteObject);
            if (value != null) {
                field.set(existingObject, value);
            }
            field.setAccessible(false);
        }

        patchObjectFields(existingObject, incompleteObject, type.getSuperclass());
    }

    public static void tokenPatcher(Token existingToken, Token incompleteToken) throws IllegalAccessException {
        patchObjectFields(existingToken, incompleteToken, Token.class);
    }

    public static void locationPatcher(Location existingLocation, Location incompleteLocation)
            throws IllegalAccessException {
        patchObjectFields(existingLocation, incompleteLocation, Location.class);
    }

    public static void evsePatcher(EVSE existingEvse, EVSE incompleteEvse) throws IllegalAccessException {
        patchObjectFields(existingEvse, incompleteEvse, EVSE.class);
    }

    public static void connectorPatcher(Connector existingConnector, Connector incompleteConnector)
            throws IllegalAccessException {
        patchObjectFields(existingConnector, incompleteConnector, Connector.class);
    }

    public static void sessionPatcher(ChargingSession existingSession, ChargingSession incompleteSession)
            throws IllegalAccessException {
        patchObjectFields(existingSession, incompleteSession, ChargingSession.class);
    }

    public static void smartLocationPatcher(SmartLocation existingLocation, SmartLocation incompleteLocation)
            throws IllegalAccessException {
        patchObjectFields(existingLocation, incompleteLocation, SmartLocation.class);
    }

}
