package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.*;
import com.banula.openlib.ocpi.model.dto.*;
import com.banula.openlib.ocpi.model.vo.Connector;
import com.banula.openlib.ocpi.model.vo.EVSE;
import com.banula.openlib.ocpi.model.vo.GeoLocation;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenericMapper {

    public <T, D> D toDTO(T entity, Class<D> dtoClass) {
        try {
            if (entity == null)
                return null;
            D dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            applyLocationCoordinatesToDTO(entity, dto);
            applyEvseCoordinatesToDTO(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity to DTO: " + e.getMessage(), e);
        }
    }

    public <D, T> T fromDTO(D dto, Class<T> entityClass) {
        try {
            if (dto == null)
                return null;
            T entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            applyLocationCoordinatesFromDTO(dto, entity);
            applyEvseCoordinatesFromDTO(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error converting DTO to entity: " + e.getMessage(), e);
        }
    }

    public <T, D> List<D> toDTOList(List<T> entities, Class<D> dtoClass) {
        List<D> dtos = new ArrayList<>();
        if (entities == null)
            return dtos;
        for (T entity : entities) {
            dtos.add(toDTO(entity, dtoClass));
        }
        return dtos;
    }

    public <D, T> List<T> fromDTOList(List<D> dtos, Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        if (dtos == null)
            return entities;
        for (D dto : dtos) {
            entities.add(fromDTO(dto, entityClass));
        }
        return entities;
    }

    // Specific model convenience methods

    public <T> CdrDTO cdrToDTO(T entity) {
        return toDTO(entity, CdrDTO.class);
    }

    public <D> CDR cdrFromDTO(D dto) {
        return fromDTO(dto, CDR.class);
    }

    public <T> List<CdrDTO> cdrListToDTOList(List<T> entities) {
        return toDTOList(entities, CdrDTO.class);
    }

    public LocationDTO locationToDTO(Location entity) {
        return toDTO(entity, LocationDTO.class);
    }

    public Location locationFromDTO(LocationDTO dto) {
        return fromDTO(dto, Location.class);
    }

    public <T> List<LocationDTO> locationListToDTOList(List<T> entities) {
        return toDTOList(entities, LocationDTO.class);
    }

    public List<Location> locationListFromDTOList(List<LocationDTO> dtos) {
        return fromDTOList(dtos, Location.class);
    }

    public <T> TariffDTO tariffToDTO(T entity) {
        return toDTO(entity, TariffDTO.class);
    }

    public <D> Tariff tariffFromDTO(D dto) {
        return fromDTO(dto, Tariff.class);
    }

    public <T> List<TariffDTO> tariffListToDTOList(List<T> entities) {
        return toDTOList(entities, TariffDTO.class);
    }

    public <T> TokenDTO tokenToDTO(T entity) {
        return toDTO(entity, TokenDTO.class);
    }

    public <D> Token tokenFromDTO(D dto) {
        return fromDTO(dto, Token.class);
    }

    public <T> List<TokenDTO> tokenListToDTOList(List<T> entities) {
        return toDTOList(entities, TokenDTO.class);
    }

    public <T> ChargingSessionDTO sessionToDTO(T entity) {
        return toDTO(entity, ChargingSessionDTO.class);
    }

    public <D> ChargingSession sessionFromDTO(D dto) {
        return fromDTO(dto, ChargingSession.class);
    }

    public <T> List<ChargingSessionDTO> sessionListToDTOList(List<T> entities) {
        return toDTOList(entities, ChargingSessionDTO.class);
    }

    public EvseDTO evseToDTO(EVSE entity) {
        return toDTO(entity, EvseDTO.class);
    }

    public EVSE evseFromDTO(EvseDTO dto) {
        return fromDTO(dto, EVSE.class);
    }

    public List<EvseDTO> evseListToDTOList(List<EVSE> entities) {
        return toDTOList(entities, EvseDTO.class);
    }

    public List<EVSE> evseListFromDTOList(List<EvseDTO> dtos) {
        return fromDTOList(dtos, EVSE.class);
    }

    public ConnectorDTO connectorToDTO(Connector entity) {
        return toDTO(entity, ConnectorDTO.class);
    }

    public Connector connectorFromDTO(ConnectorDTO dto) {
        return fromDTO(dto, Connector.class);
    }

    public List<ConnectorDTO> connectorListToDTOList(List<Connector> entities) {
        return toDTOList(entities, ConnectorDTO.class);
    }

    public List<Connector> connectorListFromDTOList(List<ConnectorDTO> dtos) {
        return fromDTOList(dtos, Connector.class);
    }

    // Internal helpers for Location coordinate conversion

    private <T, D> void applyLocationCoordinatesToDTO(T entity, D dto) {
        if (!(entity instanceof Location) || !(dto instanceof LocationDTO))
            return;
        Location location = (Location) entity;
        LocationDTO locationDTO = (LocationDTO) dto;
        if (location.getCoordinates() != null && location.getCoordinates().getCoordinates() != null) {
            List<Double> coords = location.getCoordinates().getCoordinates();
            locationDTO.setCoordinates(new GeoLocationDTO(coords.get(0), coords.get(1)));
        } else {
            locationDTO.setCoordinates(null);
        }
        if (location.getEvses() != null) {
            List<EvseDTO> evseDTOs = new ArrayList<>();
            for (EVSE evse : location.getEvses()) {
                evseDTOs.add(evseToDTO(evse));
            }
            locationDTO.setEvses(evseDTOs);
        }
    }

    private <D, T> void applyLocationCoordinatesFromDTO(D dto, T entity) {
        if (!(dto instanceof LocationDTO) || !(entity instanceof Location))
            return;
        LocationDTO locationDTO = (LocationDTO) dto;
        Location location = (Location) entity;
        GeoLocationDTO geoLocationDTO = locationDTO.getCoordinates();
        if (geoLocationDTO != null && geoLocationDTO.getLatitude() != null && geoLocationDTO.getLongitude() != null) {
            location.setCoordinates(new GeoLocation(
                    Double.parseDouble(geoLocationDTO.getLatitude()),
                    Double.parseDouble(geoLocationDTO.getLongitude())));
        } else {
            location.setCoordinates(null);
        }
        if (locationDTO.getEvses() != null) {
            List<EVSE> evses = new ArrayList<>();
            for (EvseDTO evseDTO : locationDTO.getEvses()) {
                evses.add(evseFromDTO(evseDTO));
            }
            location.setEvses(evses);
        }
    }

    private <T, D> void applyEvseCoordinatesToDTO(T entity, D dto) {
        if (!(entity instanceof EVSE) || !(dto instanceof EvseDTO))
            return;
        EVSE evse = (EVSE) entity;
        EvseDTO evseDTO = (EvseDTO) dto;
        if (evse.getCoordinates() != null && evse.getCoordinates().getCoordinates() != null) {
            List<Double> coords = evse.getCoordinates().getCoordinates();
            evseDTO.setCoordinates(new GeoLocationDTO(coords.get(0), coords.get(1)));
        } else {
            evseDTO.setCoordinates(null);
        }
        if (evse.getConnectors() != null) {
            List<ConnectorDTO> connectorDTOs = new ArrayList<>();
            for (Connector connector : evse.getConnectors()) {
                connectorDTOs.add(connectorToDTO(connector));
            }
            evseDTO.setConnectors(connectorDTOs);
        }
    }

    private <D, T> void applyEvseCoordinatesFromDTO(D dto, T entity) {
        if (!(dto instanceof EvseDTO) || !(entity instanceof EVSE))
            return;
        EvseDTO evseDTO = (EvseDTO) dto;
        EVSE evse = (EVSE) entity;
        GeoLocationDTO geoLocationDTO = evseDTO.getCoordinates();
        if (geoLocationDTO != null && geoLocationDTO.getLatitude() != null && geoLocationDTO.getLongitude() != null) {
            evse.setCoordinates(new GeoLocation(
                    Double.parseDouble(geoLocationDTO.getLatitude()),
                    Double.parseDouble(geoLocationDTO.getLongitude())));
        } else {
            evse.setCoordinates(null);
        }
        if (evseDTO.getConnectors() != null) {
            List<Connector> connectors = new ArrayList<>();
            for (ConnectorDTO connectorDTO : evseDTO.getConnectors()) {
                connectors.add(connectorFromDTO(connectorDTO));
            }
            evse.setConnectors(connectors);
        }
    }
}
