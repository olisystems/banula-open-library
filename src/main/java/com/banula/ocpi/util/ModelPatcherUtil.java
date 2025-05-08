package com.banula.ocpi.util;

import com.banula.ocpi.model.ChargingSession;
import com.banula.ocpi.model.CpoToken;
import com.banula.ocpi.model.Location;
import com.banula.ocpi.model.vo.Connector;
import com.banula.ocpi.model.vo.EVSE;

import java.lang.reflect.Field;

/*
  This model patcher methods iterates through every field of the incomplete dto request and uses the filled-in fields
  to update the existing record.
 */

public class ModelPatcherUtil {

    public static void tokenPatcher(CpoToken existingToken, CpoToken incompleteToken) throws IllegalAccessException {
        Class<?> internClass = CpoToken.class;
        Field[] internFields = internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);
            Object value=field.get(incompleteToken);
            if(value!=null){
                field.set(existingToken,value);
            }
            field.setAccessible(false);
        }
    }

    public static void locationPatcher(Location existingLocation, Location incompleteLocation) throws IllegalAccessException {
        Class<?> internClass = Location.class;
        Field[] internFields = internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);
            Object value=field.get(incompleteLocation);
            if(value!=null){
                field.set(existingLocation,value);
            }
            field.setAccessible(false);
        }
    }

    public static void evsePatcher(EVSE existingEvse, EVSE incompleteEvse) throws IllegalAccessException {
        Class<?> internClass = EVSE.class;
        Field[] internFields = internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);
            Object value=field.get(incompleteEvse);
            if(value!=null){
                field.set(existingEvse,value);
            }
            field.setAccessible(false);
        }
    }

    public static void connectorPatcher(Connector existingConnector, Connector incompleteConnector) throws IllegalAccessException {
        Class<?> internClass = Connector.class;
        Field[] internFields = internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);
            Object value=field.get(incompleteConnector);
            if(value!=null){
                field.set(existingConnector,value);
            }
            field.setAccessible(false);
        }
    }

    public static void sessionPatcher(ChargingSession existingSession, ChargingSession incompleteSession) throws IllegalAccessException {
        Class<?> internClass = ChargingSession.class;
        Field[] internFields = internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);
            Object value=field.get(incompleteSession);
            if(value!=null){
                field.set(existingSession,value);
            }
            field.setAccessible(false);
        }

    }



}
