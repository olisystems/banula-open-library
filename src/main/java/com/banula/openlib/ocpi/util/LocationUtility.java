package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.model.Location;
import com.banula.openlib.ocpi.model.dto.ConnectorDTO;
import com.banula.openlib.ocpi.model.dto.EvseDTO;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.banula.openlib.ocpi.model.vo.Connector;
import com.banula.openlib.ocpi.model.vo.EVSE;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class LocationUtility {

    @Nullable
    public static Connector findConnectorInEvseByConnectorId(String connectorId, EVSE evse) {
        if (evse.getConnectors() == null || evse.getConnectors().isEmpty()) {
            return null;
        }
        return evse.getConnectors().stream()
                .filter(connectorIteration -> Objects.equals(connectorIteration.getId(), connectorId))
                .findFirst()
                .orElse(null);
    }

    public static ConnectorDTO findConnectorDTOInEvseDTOByConnectorId(String connectorId, EvseDTO evse) {
        if (evse.getConnectors() == null || evse.getConnectors().isEmpty()) {
            return null;
        }
        return evse.getConnectors().stream()
                .filter(connectorIteration -> Objects.equals(connectorIteration.getId(), connectorId))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static EVSE findEvseInLocationByEvseUid(String evseUid, Location location) {
        if (location.getEvses() == null || location.getEvses().isEmpty()) {
            return null;
        }
        return location.getEvses().stream()
                .filter(evseIteration -> Objects.equals(evseIteration.getUid(), evseUid))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static EvseDTO findEvseDTOInLocationDTOByEvseUid(String evseUid, LocationDTO location) {
        if (location.getEvses() == null || location.getEvses().isEmpty()) {
            return null;
        }
        return location.getEvses().stream()
                .filter(evseIteration -> Objects.equals(evseIteration.getUid(), evseUid))
                .findFirst()
                .orElse(null);
    }

    public static int evseIndexOf(List<EVSE> evses, EVSE evse) {
        for (int i = 0; i < evses.size(); i++) {
            if (Objects.equals(evses.get(i).getUid(), evse.getUid())) {
                return i;
            }
        }
        return -1;
    }

    public static int evseDTOIndexOf(List<EvseDTO> evses, EvseDTO evse) {
        for (int i = 0; i < evses.size(); i++) {
            if (Objects.equals(evses.get(i).getUid(), evse.getUid())) {
                return i;
            }
        }
        return -1;
    }

    public static int connectorIndexOf(List<Connector> connectors, Connector connector) {
        for (int i = 0; i < connectors.size(); i++) {
            if (Objects.equals(connectors.get(i).getId(), connector.getId())) {
                return i;
            }
        }
        return -1;
    }

    public static int connectorDTOIndexOf(List<ConnectorDTO> connectors, ConnectorDTO connector) {
        for (int i = 0; i < connectors.size(); i++) {
            if (Objects.equals(connectors.get(i).getId(), connector.getId())) {
                return i;
            }
        }
        return -1;
    }

}
