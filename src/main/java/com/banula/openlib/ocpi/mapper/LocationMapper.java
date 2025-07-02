package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.Location;
import com.banula.openlib.ocpi.model.dto.GeoLocationDTO;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.banula.openlib.ocpi.model.vo.GeoLocation;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    static Location toLocationEntity(LocationDTO locationDTO) {

        if (locationDTO == null)
            return null;

        GeoLocation geoLocation = null;
        if (locationDTO.getCoordinates() != null) {
            GeoLocationDTO geoLocationDTO = locationDTO.getCoordinates();
            if (geoLocationDTO.getLatitude() != null && geoLocationDTO.getLongitude() != null) {
                geoLocation = new GeoLocation(
                        Double.parseDouble(locationDTO.getCoordinates().getLatitude()),
                        Double.parseDouble(locationDTO.getCoordinates().getLongitude()));
            }
        }

        Location location = new Location();
        BeanUtils.copyProperties(locationDTO, location);
        location.setCoordinates(geoLocation);
        return location;

    }

    static LocationDTO toLocationDTO(Location location) {
        if (location == null)
            return null;

        GeoLocationDTO geoLocationDTO = null;
        if (location.getCoordinates() != null && location.getCoordinates().getCoordinates() != null) {
            List<Double> coordinates = location.getCoordinates().getCoordinates();
            geoLocationDTO = new GeoLocationDTO(coordinates.get(0), coordinates.get(1));
        }

        LocationDTO locationDTO = new LocationDTO();
        BeanUtils.copyProperties(location, locationDTO);
        locationDTO.setCoordinates(geoLocationDTO);
        return locationDTO;
    }

    static List<LocationDTO> toListLocationDTO(List<Location> locations) {
        return locations == null ? null
                : locations.stream()
                        .map(LocationMapper::toLocationDTO)
                        .collect(Collectors.toList());
    }

    static List<Location> toListLocationEntity(List<LocationDTO> locationDTOs) {
        return locationDTOs == null ? null
                : locationDTOs.stream()
                        .map(LocationMapper::toLocationEntity)
                        .collect(Collectors.toList());
    }
}
