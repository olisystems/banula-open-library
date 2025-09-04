package com.banula.openlib.ocpi.custom.smartlocations.mapper;

import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.custom.smartlocations.dto.SmartLocationDTO;
import com.banula.openlib.ocpi.model.dto.GeoLocationDTO;
import com.banula.openlib.ocpi.model.vo.GeoLocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class SmartLocationMapper {

    public static SmartLocation toSmartLocationEntity(SmartLocationDTO smartLocationDTO) {

        if (smartLocationDTO == null)
            return null;

        GeoLocation geoLocation = smartLocationDTO.getCoordinates() == null
                ? null
                : new GeoLocation(Double.parseDouble(smartLocationDTO.getCoordinates().getLatitude()),
                        Double.parseDouble(smartLocationDTO.getCoordinates().getLongitude()));

        SmartLocation smartLocation = new SmartLocation();
        BeanUtils.copyProperties(smartLocationDTO, smartLocation);
        smartLocation.setCoordinates(geoLocation);
        return smartLocation;
    }

    public static SmartLocationDTO toSmartLocationDTO(SmartLocation location) {
        if (location == null)
            return null;

        GeoLocationDTO geoLocationDTO = location.getCoordinates() == null ? null
                : location.getCoordinates().getCoordinates() == null ? null
                        : new GeoLocationDTO(location.getCoordinates().getCoordinates().get(0),
                                location.getCoordinates().getCoordinates().get(1));

        SmartLocationDTO smartLocationDTO = new SmartLocationDTO();
        BeanUtils.copyProperties(location, smartLocationDTO);
        smartLocationDTO.setCoordinates(geoLocationDTO);
        return smartLocationDTO;
    }

    public static List<SmartLocationDTO> toListSmartLocationDTO(List<SmartLocation> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(SmartLocationMapper::toSmartLocationDTO)
                .collect(Collectors.toList());
    }

    public static List<SmartLocation> toListSmartLocation(List<SmartLocationDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(SmartLocationMapper::toSmartLocationEntity)
                .collect(Collectors.toList());
    }
}