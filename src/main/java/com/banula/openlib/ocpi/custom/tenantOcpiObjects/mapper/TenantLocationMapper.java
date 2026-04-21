package com.banula.openlib.ocpi.custom.tenantOcpiObjects.mapper;

import com.banula.openlib.ocpi.custom.tenantOcpiObjects.TenantLocation;
import com.banula.openlib.ocpi.custom.tenantOcpiObjects.dto.TenantLocationDTO;
import com.banula.openlib.ocpi.model.dto.GeoLocationDTO;
import com.banula.openlib.ocpi.model.vo.GeoLocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class TenantLocationMapper {

    public static TenantLocation toTenantLocationEntity(TenantLocationDTO tenantLocationDTO) {

        if (tenantLocationDTO == null)
            return null;

        GeoLocation geoLocation = tenantLocationDTO.getCoordinates() == null
                ? null
                : new GeoLocation(Double.parseDouble(tenantLocationDTO.getCoordinates().getLatitude()),
                        Double.parseDouble(tenantLocationDTO.getCoordinates().getLongitude()));

        TenantLocation tenantLocation = new TenantLocation();
        BeanUtils.copyProperties(tenantLocationDTO, tenantLocation);
        tenantLocation.setCoordinates(geoLocation);
        return tenantLocation;
    }

    public static TenantLocationDTO toTenantLocationDTO(TenantLocation location) {
        if (location == null)
            return null;

        GeoLocationDTO geoLocationDTO = location.getCoordinates() == null ? null
                : location.getCoordinates().getCoordinates() == null ? null
                        : new GeoLocationDTO(location.getCoordinates().getCoordinates().get(0),
                                location.getCoordinates().getCoordinates().get(1));

        TenantLocationDTO tenantLocationDTO = new TenantLocationDTO();
        BeanUtils.copyProperties(location, tenantLocationDTO);
        tenantLocationDTO.setCoordinates(geoLocationDTO);
        return tenantLocationDTO;
    }

    public static List<TenantLocationDTO> toListTenantLocationDTO(List<TenantLocation> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(TenantLocationMapper::toTenantLocationDTO)
                .collect(Collectors.toList());
    }

    public static List<TenantLocation> toListTenantLocation(List<TenantLocationDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(TenantLocationMapper::toTenantLocationEntity)
                .collect(Collectors.toList());
    }
}
