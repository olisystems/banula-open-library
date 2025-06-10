package com.banula.openlib.ocpi.custom.smartlocations.mapper;

import com.banula.openlib.ocpi.custom.smartlocations.SmartLocation;
import com.banula.openlib.ocpi.custom.smartlocations.dto.SmartLocationDTO;
import com.banula.openlib.ocpi.model.dto.GeoLocationDTO;
import com.banula.openlib.ocpi.model.vo.GeoLocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SmartLocationMapper {

    public static SmartLocation toSmartLocationEntity(SmartLocationDTO smartLocationDTO) {

        if (smartLocationDTO == null)
            return null;

        GeoLocation geoLocation = smartLocationDTO.getCoordinates() == null
                ? null
                : new GeoLocation(Double.parseDouble(smartLocationDTO.getCoordinates().getLatitude()),
                        Double.parseDouble(smartLocationDTO.getCoordinates().getLongitude()));

        return SmartLocation.builder()
                .id(smartLocationDTO.getId())
                .city(smartLocationDTO.getCity())
                .address(smartLocationDTO.getAddress())
                .evses(smartLocationDTO.getEvses())
                .coordinates(geoLocation)
                .energyMix(smartLocationDTO.getEnergyMix())
                .name(smartLocationDTO.getName())
                .countryCode(smartLocationDTO.getCountryCode())
                .country(smartLocationDTO.getCountry())
                .lastUpdated(smartLocationDTO.getLastUpdated())
                .owner(smartLocationDTO.getOwner())
                .relatedLocations(smartLocationDTO.getRelatedLocations())
                .directions(smartLocationDTO.getDirections())
                .images(smartLocationDTO.getImages())
                .facilities(smartLocationDTO.getFacilities())
                .openingTimes(smartLocationDTO.getOpeningTimes())
                .operator(smartLocationDTO.getOperator())
                .state(smartLocationDTO.getState())
                .parkingType(smartLocationDTO.getParkingType())
                .publish(smartLocationDTO.getPublish())
                .postalCode(smartLocationDTO.getPostalCode())
                .partyId(smartLocationDTO.getPartyId())
                .publishAllowedTo(smartLocationDTO.getPublishAllowedTo())
                .subOperator(smartLocationDTO.getSubOperator())
                .timeZone(smartLocationDTO.getTimeZone())
                .chargingWhenClosed(smartLocationDTO.getChargingWhenClosed())
                // Smart Location Fields
                .defaultSupplier(smartLocationDTO.getDefaultSupplier())
                .malo(smartLocationDTO.getMalo())
                .smartMeterId(smartLocationDTO.getSmartMeterId())
                .messageQueueUrl(smartLocationDTO.getMessageQueueUrl())
                .build();
    }

    public static SmartLocationDTO toSmartLocationDTO(SmartLocation location) {
        if (location == null)
            return null;

        GeoLocationDTO geoLocationDTO = location.getCoordinates() == null ? null
                : location.getCoordinates().getCoordinates() == null ? null
                        : new GeoLocationDTO(location.getCoordinates().getCoordinates().get(0),
                                location.getCoordinates().getCoordinates().get(1));

        return SmartLocationDTO.builder()
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
                // Smart Location Fields
                .defaultSupplier(location.getDefaultSupplier())
                .malo(location.getMalo())
                .smartMeterId(location.getSmartMeterId())
                .messageQueueUrl(location.getMessageQueueUrl())
                .build();
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