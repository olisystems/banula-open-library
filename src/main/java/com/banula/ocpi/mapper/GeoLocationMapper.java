package com.banula.ocpi.mapper;


import com.banula.ocpi.model.dto.GeoLocationDTO;
import com.banula.ocpi.model.vo.GeoLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeoLocationMapper {

    static GeoLocationDTO toGeoLocationDTO(GeoLocation geoLocation) {
        if (geoLocation == null) return null;
        return new GeoLocationDTO(geoLocation.getCoordinates().get(0), geoLocation.getCoordinates().get(1));
    }

    static GeoLocation toGeoLocationEntity(GeoLocationDTO geoLocationDTO) {
        if (geoLocationDTO == null) return null;
        return new GeoLocation(Double.parseDouble(geoLocationDTO.getLatitude()), Double.parseDouble(geoLocationDTO.getLongitude()));
    }
}