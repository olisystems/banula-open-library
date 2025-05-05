package com.banula.mapper;

import com.banula.ocpi.mapper.TariffMapper;
import com.banula.ocpi.model.Tariff;
import com.banula.ocpi.model.dto.TariffDTO;
import com.banula.ocpi.model.enums.TariffType;
import com.banula.ocpi.model.vo.EnergyMix;
import com.banula.ocpi.model.vo.Price;
import com.banula.ocpi.model.vo.TariffElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TariffMapperTest {

    private TariffDTO tariffDTO;
    private Tariff tariffEntity;

    @BeforeEach
    void setUp() {
        // Initialize mock TariffDTO object
        tariffDTO = TariffDTO.builder()
                .id("tariff123")
                .countryCode("DE")
                .partyId("ABC")
                .currency("EUR")
                .type(TariffType.REGULAR)
                .energyMix(new EnergyMix())
                .tariffAltText(Collections.emptyList())
                .elements(Collections.singletonList(new TariffElement()))
                .minPrice(new Price(5.0f, 5.5f))
                .maxPrice(new Price(15.0f, 16.5f))
                .tariffAltUrl("https://tariff.url")
                .lastUpdated(LocalDateTime.of(2024, 9, 26, 12, 0))
                .startDateTime(LocalDateTime.of(2024, 9, 26, 10, 0))
                .endDateTime(LocalDateTime.of(2024, 9, 27, 10, 0))
                .build();

        // Initialize mock Tariff entity object
        tariffEntity = Tariff.builder()
                .id("tariff123")
                .countryCode("DE")
                .partyId("ABC")
                .currency("EUR")
                .type(TariffType.REGULAR)
                .energyMix(new EnergyMix())
                .tariffAltText(Collections.emptyList())
                .elements(Collections.singletonList(new TariffElement()))
                .minPrice(new Price(5.0f, 5.5f))
                .maxPrice(new Price(15.0f, 16.5f))
                .tariffAltUrl("https://tariff.url")
                .lastUpdated(LocalDateTime.of(2024, 9, 26, 12, 0))
                .startDateTime(LocalDateTime.of(2024, 9, 26, 10, 0))
                .endDateTime(LocalDateTime.of(2024, 9, 27, 10, 0))
                .build();
    }

    @Test
    void testToTariffEntity() {
        Tariff mappedEntity = TariffMapper.toTariffEntity(tariffDTO);
        assertNotNull(mappedEntity);
        assertEquals(tariffDTO.getId(), mappedEntity.getId());
        assertEquals(tariffDTO.getCountryCode(), mappedEntity.getCountryCode());
        assertEquals(tariffDTO.getPartyId(), mappedEntity.getPartyId());
        assertEquals(tariffDTO.getCurrency(), mappedEntity.getCurrency());
        assertEquals(tariffDTO.getType(), mappedEntity.getType());
        assertEquals(tariffDTO.getEnergyMix(), mappedEntity.getEnergyMix());
        assertEquals(tariffDTO.getTariffAltText(), mappedEntity.getTariffAltText());
        assertEquals(tariffDTO.getElements(), mappedEntity.getElements());
        assertEquals(tariffDTO.getMinPrice(), mappedEntity.getMinPrice());
        assertEquals(tariffDTO.getMaxPrice(), mappedEntity.getMaxPrice());
        assertEquals(tariffDTO.getTariffAltUrl(), mappedEntity.getTariffAltUrl());
        assertEquals(tariffDTO.getLastUpdated(), mappedEntity.getLastUpdated());
        assertEquals(tariffDTO.getStartDateTime(), mappedEntity.getStartDateTime());
        assertEquals(tariffDTO.getEndDateTime(), mappedEntity.getEndDateTime());
    }

    @Test
    void testToTariffDTO() {
        TariffDTO mappedDTO = TariffMapper.toTariffDTO(tariffEntity);
        assertNotNull(mappedDTO);
        assertEquals(tariffEntity.getId(), mappedDTO.getId());
        assertEquals(tariffEntity.getCountryCode(), mappedDTO.getCountryCode());
        assertEquals(tariffEntity.getPartyId(), mappedDTO.getPartyId());
        assertEquals(tariffEntity.getCurrency(), mappedDTO.getCurrency());
        assertEquals(tariffEntity.getType(), mappedDTO.getType());
        assertEquals(tariffEntity.getEnergyMix(), mappedDTO.getEnergyMix());
        assertEquals(tariffEntity.getTariffAltText(), mappedDTO.getTariffAltText());
        assertEquals(tariffEntity.getElements(), mappedDTO.getElements());
        assertEquals(tariffEntity.getMinPrice(), mappedDTO.getMinPrice());
        assertEquals(tariffEntity.getMaxPrice(), mappedDTO.getMaxPrice());
        assertEquals(tariffEntity.getTariffAltUrl(), mappedDTO.getTariffAltUrl());
        assertEquals(tariffEntity.getLastUpdated(), mappedDTO.getLastUpdated());
        assertEquals(tariffEntity.getStartDateTime(), mappedDTO.getStartDateTime());
        assertEquals(tariffEntity.getEndDateTime(), mappedDTO.getEndDateTime());
    }

    @Test
    void testToTariffEntity_Null() {
        Tariff mappedEntity = TariffMapper.toTariffEntity(null);
        assertNull(mappedEntity);
    }

    @Test
    void testToTariffDTO_Null() {
        TariffDTO mappedDTO = TariffMapper.toTariffDTO(null);
        assertNull(mappedDTO);
    }

    @Test
    void testToListTariffDTO() {
        List<Tariff> tariffEntities = Collections.singletonList(tariffEntity);
        List<TariffDTO> tariffDTOs = TariffMapper.toListTariffDTO(tariffEntities);
        assertNotNull(tariffDTOs);
        assertEquals(1, tariffDTOs.size());
        assertEquals(tariffEntity.getId(), tariffDTOs.get(0).getId());
    }

    @Test
    void testToListTariff() {
        List<TariffDTO> tariffDTOs = Collections.singletonList(tariffDTO);
        List<Tariff> tariffEntities = TariffMapper.toListTariff(tariffDTOs);
        assertNotNull(tariffEntities);
        assertEquals(1, tariffEntities.size());
        assertEquals(tariffDTO.getId(), tariffEntities.get(0).getId());
    }

    @Test
    void testToListTariffDTO_Null() {
        List<TariffDTO> tariffDTOs = TariffMapper.toListTariffDTO(null);
        assertNull(tariffDTOs);
    }

    @Test
    void testToListTariff_Null() {
        List<Tariff> tariffEntities = TariffMapper.toListTariff(null);
        assertNull(tariffEntities);
    }
}
