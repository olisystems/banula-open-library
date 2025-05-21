package com.banula.ocpi.mapper;

import com.banula.ocpi.model.Location;
import com.banula.ocpi.model.dto.GeoLocationDTO;
import com.banula.ocpi.model.dto.LocationDTO;
import com.banula.ocpi.model.vo.GeoLocation;
import org.mapstruct.Mapper;

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

        return Location.builder()
                .id(locationDTO.getId())
                .city(locationDTO.getCity())
                .address(locationDTO.getAddress())
                .evses(locationDTO.getEvses())
                .coordinates(geoLocation)
                .energyMix(locationDTO.getEnergyMix())
                .name(locationDTO.getName())
                .countryCode(locationDTO.getCountryCode())
                .country(locationDTO.getCountry())
                .lastUpdated(locationDTO.getLastUpdated())
                .owner(locationDTO.getOwner())
                .relatedLocations(locationDTO.getRelatedLocations())
                .directions(locationDTO.getDirections())
                .images(locationDTO.getImages())
                .facilities(locationDTO.getFacilities())
                .openingTimes(locationDTO.getOpeningTimes())
                .operator(locationDTO.getOperator())
                .state(locationDTO.getState())
                .parkingType(locationDTO.getParkingType())
                .publish(locationDTO.getPublish())
                .postalCode(locationDTO.getPostalCode())
                .partyId(locationDTO.getPartyId())
                .publishAllowedTo(locationDTO.getPublishAllowedTo())
                .subOperator(locationDTO.getSubOperator())
                .timeZone(locationDTO.getTimeZone())
                .chargingWhenClosed(locationDTO.getChargingWhenClosed())
                .build();
    }

    static LocationDTO toLocationDTO(Location location) {
        if (location == null)
            return null;

        GeoLocationDTO geoLocationDTO = null;
        if (location.getCoordinates() != null && location.getCoordinates().getCoordinates() != null) {
            List<Double> coordinates = location.getCoordinates().getCoordinates();
            geoLocationDTO = new GeoLocationDTO(coordinates.get(0), coordinates.get(1));
        }

        return LocationDTO.builder()
                .id(location.getId())
                .city(location.getCity())
                .address(location.getAddress())
                .evses(location.getEvses())
                .coordinates(geoLocationDTO)
                .energyMix(location.getEnergyMix())
                .name(location.getName())
                .countryCode(location.getCountryCode())
                .country(location.getCountry())
                .lastUpdated(location.getLastUpdated())
                .owner(location.getOwner())
                .relatedLocations(location.getRelatedLocations())
                .directions(location.getDirections())
                .images(location.getImages())
                .facilities(location.getFacilities())
                .openingTimes(location.getOpeningTimes())
                .operator(location.getOperator())
                .state(location.getState())
                .parkingType(location.getParkingType())
                .publish(location.getPublish())
                .postalCode(location.getPostalCode())
                .partyId(location.getPartyId())
                .publishAllowedTo(location.getPublishAllowedTo())
                .subOperator(location.getSubOperator())
                .timeZone(location.getTimeZone())
                .chargingWhenClosed(location.getChargingWhenClosed())
                .build();
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
