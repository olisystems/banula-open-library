package com.banula.mapper;

import com.banula.ocpi.mapper.LocationMapper;
import com.banula.ocpi.model.Location;
import com.banula.ocpi.model.dto.GeoLocationDTO;
import com.banula.ocpi.model.dto.LocationDTO;
import com.banula.ocpi.model.enums.AvailableFlexibility;
import com.banula.ocpi.model.enums.ParkingType;
import com.banula.ocpi.model.vo.BusinessDetails;
import com.banula.ocpi.model.vo.EnergyMix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationMapperTest {

    @Test
    void generateLocation_from_LocationDTO() {
        LocationDTO locationDTO = createSampleLocationDto();
        Location result = LocationMapper.toLocationEntity(locationDTO);
        assertNotNull(result);
        Assertions.assertEquals(locationDTO.getCountryCode(), result.getCountryCode());
        Assertions.assertEquals(locationDTO.getPartyId(), result.getPartyId());
        Assertions.assertEquals(locationDTO.getId(), result.getId());
        Assertions.assertEquals(locationDTO.getPublish(), result.getPublish());
        Assertions.assertEquals(locationDTO.getPublishAllowedTo(), result.getPublishAllowedTo());
        Assertions.assertEquals(locationDTO.getName(), result.getName());
        Assertions.assertEquals(locationDTO.getAddress(), result.getAddress());
        Assertions.assertEquals(locationDTO.getCity(), result.getCity());
        Assertions.assertEquals(locationDTO.getPostalCode(), result.getPostalCode());
        Assertions.assertEquals(locationDTO.getState(), result.getState());
        Assertions.assertEquals(locationDTO.getCountry(), result.getCountry());
        Assertions.assertEquals(Double.parseDouble(locationDTO.getCoordinates().getLatitude()), result.getCoordinates().getCoordinates().get(0));
        Assertions.assertEquals(Double.parseDouble(locationDTO.getCoordinates().getLongitude()), result.getCoordinates().getCoordinates().get(1));
        Assertions.assertEquals(locationDTO.getParkingType(), result.getParkingType());
        Assertions.assertEquals(locationDTO.getEvses(), result.getEvses());
        Assertions.assertEquals(locationDTO.getDirections(), result.getDirections());
        Assertions.assertEquals(locationDTO.getOperator(), result.getOperator());
        Assertions.assertEquals(locationDTO.getSubOperator(), result.getSubOperator());
        Assertions.assertEquals(locationDTO.getOwner(), result.getOwner());
        Assertions.assertEquals(locationDTO.getFacilities(), result.getFacilities());
        Assertions.assertEquals(locationDTO.getTimeZone(), result.getTimeZone());
        Assertions.assertEquals(locationDTO.getEnergyMix(), result.getEnergyMix());
        Assertions.assertEquals(locationDTO.getLastUpdated(), result.getLastUpdated());
    }

    @Test
    void generateLocation_with_FlexibilityDescription_AsEnum() {
        LocationDTO locationDTO = createSampleLocationDto();
        locationDTO.getEnergyMix().setAvailableFlexibility(AvailableFlexibility.MIXED_LOADS);

        Location result = LocationMapper.toLocationEntity(locationDTO);
        AvailableFlexibility availableFlexibility = result.getEnergyMix().getAvailableFlexibility();

        Assertions.assertEquals(AvailableFlexibility.MIXED_LOADS, availableFlexibility);
    }

    @Test
    void generateLocation_with_FlexibilityDescription_AsString() {
        LocationDTO locationDTO = createSampleLocationDto();
        locationDTO.getEnergyMix().setAvailableFlexibility(AvailableFlexibility.NOT_AVAILABLE);

        Location result = LocationMapper.toLocationEntity(locationDTO);

        String productName = result.getEnergyMix().getEnergyProductName();

        Assertions.assertEquals("NotAvailable", productName);
    }

    @Test
    void generateLocation_with_FlexibilityDescription_Invalid() {
        LocationDTO locationDTO = createSampleLocationDto();
        locationDTO.getEnergyMix().setEnergyProductName("Product Name");
        Location result = LocationMapper.toLocationEntity(locationDTO);

        AvailableFlexibility availableFlexibility = result.getEnergyMix().getAvailableFlexibility();
        String productName = result.getEnergyMix().getEnergyProductName();

        Assertions.assertEquals(AvailableFlexibility.INVALID, availableFlexibility);
        Assertions.assertEquals("Product Name", productName);
    }

    @Test
    void generateLocation_with_EnergySupplier() {
        LocationDTO locationDTO = createSampleLocationDto();
        locationDTO.getEnergyMix().setSupplierName("Supplier Name");
        Location result = LocationMapper.toLocationEntity(locationDTO);

        String supplierName = result.getEnergyMix().getSupplierName();

        Assertions.assertEquals("Supplier Name", supplierName);
    }

    // New Tests for List Conversion
    @Test
    void testToListLocationDTO() {
        Location location = LocationMapper.toLocationEntity(createSampleLocationDto());
        List<Location> locationList = Collections.singletonList(location);
        List<LocationDTO> locationDTOList = LocationMapper.toListLocationDTO(locationList);
        assertNotNull(locationDTOList);
        assertEquals(1, locationDTOList.size());
        Assertions.assertEquals(location.getId(), locationDTOList.get(0).getId());
    }

    @Test
    void testToListLocationEntity() {
        LocationDTO locationDTO = createSampleLocationDto();
        List<LocationDTO> locationDTOList = Collections.singletonList(locationDTO);
        List<Location> locationList = LocationMapper.toListLocationEntity(locationDTOList);
        assertNotNull(locationList);
        assertEquals(1, locationList.size());
        Assertions.assertEquals(locationDTO.getId(), locationList.get(0).getId());
    }

    @Test
    void testToListLocationDTO_Null() {
        List<LocationDTO> locationDTOList = LocationMapper.toListLocationDTO(null);
        assertNull(locationDTOList);
    }

    @Test
    void testToListLocationEntity_Null() {
        List<Location> locationList = LocationMapper.toListLocationEntity(null);
        assertNull(locationList);
    }

    private LocationDTO createSampleLocationDto() {
        EnergyMix energyMix = new EnergyMix();
        energyMix.setEnergySources(List.of());
        energyMix.setEnvironImpact(List.of());
        energyMix.setGreenEnergy(true);
        energyMix.setSupplierName("SupplierName");
        energyMix.setEnergyProductName("EnergyProductName");

        BusinessDetails operator = new BusinessDetails();
        operator.setName("OPERATOR_NAME");
        operator.setWebsite("OPERATOR_WEBSITE");

        BusinessDetails suboperator = new BusinessDetails();
        suboperator.setName("SUBOPERATOR_NAME");
        suboperator.setWebsite("SUBOPERATOR_WEBSITE");

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCountryCode("DE");
        locationDTO.setPartyId("DE");
        locationDTO.setId("ID");
        locationDTO.setPublish(true);
        locationDTO.setPublishAllowedTo(List.of());
        locationDTO.setName("Name");
        locationDTO.setAddress("Address");
        locationDTO.setCity("City");
        locationDTO.setPostalCode("12345");
        locationDTO.setState("State");
        locationDTO.setCountry("Country");
        locationDTO.setCoordinates(new GeoLocationDTO(1.0, 1.0));
        locationDTO.setParkingType(ParkingType.PARKING_GARAGE);
        locationDTO.setEvses(List.of());
        locationDTO.setDirections(List.of());
        locationDTO.setOperator(operator);
        locationDTO.setSubOperator(suboperator);
        locationDTO.setOwner(operator);
        locationDTO.setFacilities(List.of());
        locationDTO.setTimeZone("UTC+2");
        locationDTO.setEnergyMix(energyMix);
        locationDTO.setLastUpdated(LocalDateTime.now());

        return locationDTO;
    }
}
