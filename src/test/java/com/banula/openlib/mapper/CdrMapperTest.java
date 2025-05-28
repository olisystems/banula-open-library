package com.banula.openlib.mapper;

import com.banula.openlib.ocpi.mapper.CdrMapper;
import com.banula.openlib.ocpi.model.CDR;
import com.banula.openlib.ocpi.model.Tariff;
import com.banula.openlib.ocpi.model.dto.CdrDTO;
import com.banula.openlib.ocpi.model.dto.GeoLocationDTO;
import com.banula.openlib.ocpi.model.enums.*;
import com.banula.openlib.ocpi.model.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CdrMapperTest {

        private CdrDTO cdrDTO;
        private CDR cdrEntity;

        @BeforeEach
        void setUp() {
                // Mock CdrDTO object
                cdrDTO = CdrDTO.builder()
                                .countryCode("DE")
                                .partyId("ABC")
                                .id("id123")
                                .startDateTime(LocalDateTime.of(2024, 9, 26, 10, 0))
                                .endDateTime(LocalDateTime.of(2024, 9, 26, 12, 0))
                                .sessionId("session123")
                                .cdrToken(CdrToken.builder()
                                                .uid("token123")
                                                .type(TokenType.AD_HOC_USER)
                                                .contractId("contract123")
                                                .countryCode("DE")
                                                .partyId("ABC")
                                                .build())
                                .authMethod(AuthMethod.AUTH_REQUEST)
                                .authorizationReference("authRef123")
                                .cdrLocation(CdrLocation.builder()
                                                .id("location123")
                                                .name("Test Location")
                                                .address("123 Test St")
                                                .city("TestCity")
                                                .postalCode("12345")
                                                .country("DE")
                                                .coordinates(new GeoLocationDTO(52.5200, 13.4050))
                                                .evseUid("evse123")
                                                .evseId("evseID123")
                                                .connectorId("connector123")
                                                .connectorStandard(ConnectorType.CHADEMO)
                                                .connectorFormat(ConnectorFormat.SOCKET)
                                                .connectorPowerType(PowerType.AC_1_PHASE)
                                                .build())
                                .meterId("meterId123")
                                .currency("EUR")
                                .tariffs(Collections.singletonList(Tariff.builder()
                                                .countryCode("DE")
                                                .partyId("ABC")
                                                .id("tariff123")
                                                .currency("EUR")
                                                .elements(Collections.singletonList(new TariffElement(
                                                                Collections.singletonList(
                                                                                new PriceComponent(
                                                                                                TariffDimensionType.ENERGY,
                                                                                                0.20f, 1)))))
                                                .lastUpdated(LocalDateTime.of(2024, 9, 26, 12, 0))
                                                .build()))
                                .chargingPeriods(Collections.singletonList(
                                                new ChargingPeriod(
                                                                LocalDateTime.of(2024, 9, 26, 10, 0),
                                                                Collections.singletonList(new CdrDimension(
                                                                                CdrDimensionType.ENERGY, 50.0f)),
                                                                "tariff123")))
                                .signedData(SignedData.builder()
                                                .encodingMethod("SHA256")
                                                .encodingMethodVersion(1)
                                                .publicKey("publicKey123")
                                                .signedValues(Collections.singletonList(new SignedValue("End",
                                                                "plainData123", "signedData123")))
                                                .url("https://signeddata.com")
                                                .build())
                                .totalCost(new Price(25.50f, 30.00f)) // Including VAT
                                .totalFixedCost(new Price(5.00f, 6.00f))
                                .totalEnergy(100.0f)
                                .totalEnergyCost(new Price(15.00f, 18.00f))
                                .totalTime(120.0f)
                                .totalTimeCost(new Price(10.00f, 12.00f))
                                .totalParkingTime(60.0f)
                                .totalParkingCost(new Price(2.00f, 2.40f))
                                .totalReservationCost(new Price(3.00f, 3.60f))
                                .remark("Test Remark")
                                .invoiceReferenceId("invoiceRef123")
                                .credit(true)
                                .creditReferenceId("creditRef123")
                                .lastUpdated(LocalDateTime.of(2024, 9, 26, 12, 0))
                                .build();

                // Mock CDR entity object
                cdrEntity = CDR.builder()
                                .countryCode("DE")
                                .partyId("ABC")
                                .id("id123")
                                .startDateTime(LocalDateTime.of(2024, 9, 26, 10, 0))
                                .endDateTime(LocalDateTime.of(2024, 9, 26, 12, 0))
                                .sessionId("session123")
                                .cdrToken(CdrToken.builder()
                                                .uid("token123")
                                                .type(TokenType.AD_HOC_USER)
                                                .contractId("contract123")
                                                .countryCode("DE")
                                                .partyId("ABC")
                                                .build())
                                .authMethod(AuthMethod.AUTH_REQUEST)
                                .authorizationReference("authRef123")
                                .cdrLocation(CdrLocation.builder()
                                                .id("location123")
                                                .name("Test Location")
                                                .address("123 Test St")
                                                .city("TestCity")
                                                .postalCode("12345")
                                                .country("DE")
                                                .coordinates(new GeoLocationDTO(52.5200, 13.4050))
                                                .evseUid("evse123")
                                                .evseId("evseID123")
                                                .connectorId("connector123")
                                                .connectorStandard(ConnectorType.CHADEMO)
                                                .connectorFormat(ConnectorFormat.SOCKET)
                                                .connectorPowerType(PowerType.AC_1_PHASE)
                                                .build())
                                .meterId("meterId123")
                                .currency("EUR")
                                .tariffs(Collections.singletonList(Tariff.builder()
                                                .countryCode("DE")
                                                .partyId("ABC")
                                                .id("tariff123")
                                                .currency("EUR")
                                                .elements(Collections.singletonList(new TariffElement(
                                                                Collections.singletonList(
                                                                                new PriceComponent(
                                                                                                TariffDimensionType.ENERGY,
                                                                                                0.20f, 1)))))
                                                .lastUpdated(LocalDateTime.of(2024, 9, 26, 12, 0))
                                                .build()))
                                .chargingPeriods(Collections.singletonList(
                                                new ChargingPeriod(
                                                                LocalDateTime.of(2024, 9, 26, 10, 0),
                                                                Collections.singletonList(new CdrDimension(
                                                                                CdrDimensionType.ENERGY, 50.0f)),
                                                                "tariff123")))
                                .signedData(SignedData.builder()
                                                .encodingMethod("SHA256")
                                                .encodingMethodVersion(1)
                                                .publicKey("publicKey123")
                                                .signedValues(Collections.singletonList(new SignedValue("End",
                                                                "plainData123", "signedData123")))
                                                .url("https://signeddata.com")
                                                .build())
                                .totalCost(new Price(25.50f, 30.00f)) // Including VAT
                                .totalFixedCost(new Price(5.00f, 6.00f))
                                .totalEnergy(100.0f)
                                .totalEnergyCost(new Price(15.00f, 18.00f))
                                .totalTime(120.0f)
                                .totalTimeCost(new Price(10.00f, 12.00f))
                                .totalParkingTime(60.0f)
                                .totalParkingCost(new Price(2.00f, 2.40f))
                                .totalReservationCost(new Price(3.00f, 3.60f))
                                .remark("Test Remark")
                                .invoiceReferenceId("invoiceRef123")
                                .credit(true)
                                .creditReferenceId("creditRef123")
                                .lastUpdated(LocalDateTime.of(2024, 9, 26, 12, 0))
                                .build();
        }

        @Test
        void testToCdrEntity() {
                CDR mappedEntity = CdrMapper.toCdrEntity(cdrDTO);
                assertNotNull(mappedEntity);
                assertEquals(cdrDTO.getCountryCode(), mappedEntity.getCountryCode());
                assertEquals(cdrDTO.getPartyId(), mappedEntity.getPartyId());
                assertEquals(cdrDTO.getId(), mappedEntity.getId());
                assertEquals(cdrDTO.getStartDateTime(), mappedEntity.getStartDateTime());
                assertEquals(cdrDTO.getEndDateTime(), mappedEntity.getEndDateTime());
                assertEquals(cdrDTO.getSessionId(), mappedEntity.getSessionId());
                assertEquals(cdrDTO.getCdrToken(), mappedEntity.getCdrToken());
                assertEquals(cdrDTO.getAuthMethod(), mappedEntity.getAuthMethod());
                assertEquals(cdrDTO.getAuthorizationReference(), mappedEntity.getAuthorizationReference());
                assertEquals(cdrDTO.getCdrLocation(), mappedEntity.getCdrLocation());
                assertEquals(cdrDTO.getMeterId(), mappedEntity.getMeterId());
                assertEquals(cdrDTO.getCurrency(), mappedEntity.getCurrency());
                assertEquals(cdrDTO.getTariffs(), mappedEntity.getTariffs());
                assertEquals(cdrDTO.getChargingPeriods(), mappedEntity.getChargingPeriods());
                assertEquals(cdrDTO.getSignedData(), mappedEntity.getSignedData());
                assertEquals(cdrDTO.getTotalCost(), mappedEntity.getTotalCost());
                assertEquals(cdrDTO.getTotalFixedCost(), mappedEntity.getTotalFixedCost());
                assertEquals(cdrDTO.getTotalEnergy(), mappedEntity.getTotalEnergy());
                assertEquals(cdrDTO.getTotalEnergyCost(), mappedEntity.getTotalEnergyCost());
                assertEquals(cdrDTO.getTotalTime(), mappedEntity.getTotalTime());
                assertEquals(cdrDTO.getTotalTimeCost(), mappedEntity.getTotalTimeCost());
                assertEquals(cdrDTO.getTotalParkingTime(), mappedEntity.getTotalParkingTime());
                assertEquals(cdrDTO.getTotalParkingCost(), mappedEntity.getTotalParkingCost());
                assertEquals(cdrDTO.getTotalReservationCost(), mappedEntity.getTotalReservationCost());
                assertEquals(cdrDTO.getRemark(), mappedEntity.getRemark());
                assertEquals(cdrDTO.getInvoiceReferenceId(), mappedEntity.getInvoiceReferenceId());
                assertEquals(cdrDTO.getCredit(), mappedEntity.getCredit());
                assertEquals(cdrDTO.getCreditReferenceId(), mappedEntity.getCreditReferenceId());
                assertEquals(cdrDTO.getLastUpdated(), mappedEntity.getLastUpdated());
        }

        @Test
        void testToCdrDTO() {
                CdrDTO mappedDTO = CdrMapper.toCdrDTO(cdrEntity);
                assertNotNull(mappedDTO);
                assertEquals(cdrEntity.getCountryCode(), mappedDTO.getCountryCode());
                assertEquals(cdrEntity.getPartyId(), mappedDTO.getPartyId());
                assertEquals(cdrEntity.getId(), mappedDTO.getId());
                assertEquals(cdrEntity.getStartDateTime(), mappedDTO.getStartDateTime());
                assertEquals(cdrEntity.getEndDateTime(), mappedDTO.getEndDateTime());
                assertEquals(cdrEntity.getSessionId(), mappedDTO.getSessionId());
                assertEquals(cdrEntity.getCdrToken(), mappedDTO.getCdrToken());
                assertEquals(cdrEntity.getAuthMethod(), mappedDTO.getAuthMethod());
                assertEquals(cdrEntity.getCdrLocation(), mappedDTO.getCdrLocation());
                assertEquals(cdrEntity.getMeterId(), mappedDTO.getMeterId());
                assertEquals(cdrEntity.getCurrency(), mappedDTO.getCurrency());
                assertEquals(cdrEntity.getTariffs(), mappedDTO.getTariffs());
                assertEquals(cdrEntity.getChargingPeriods(), mappedDTO.getChargingPeriods());
                assertEquals(cdrEntity.getSignedData(), mappedDTO.getSignedData());
                assertEquals(cdrEntity.getTotalCost(), mappedDTO.getTotalCost());
                assertEquals(cdrEntity.getTotalFixedCost(), mappedDTO.getTotalFixedCost());
                assertEquals(cdrEntity.getTotalEnergy(), mappedDTO.getTotalEnergy());
                assertEquals(cdrEntity.getTotalEnergyCost(), mappedDTO.getTotalEnergyCost());
                assertEquals(cdrEntity.getTotalTime(), mappedDTO.getTotalTime());
                assertEquals(cdrEntity.getTotalTimeCost(), mappedDTO.getTotalTimeCost());
                assertEquals(cdrEntity.getTotalParkingTime(), mappedDTO.getTotalParkingTime());
                assertEquals(cdrEntity.getTotalParkingCost(), mappedDTO.getTotalParkingCost());
                assertEquals(cdrEntity.getTotalReservationCost(), mappedDTO.getTotalReservationCost());
                assertEquals(cdrEntity.getRemark(), mappedDTO.getRemark());
                assertEquals(cdrEntity.getInvoiceReferenceId(), mappedDTO.getInvoiceReferenceId());
                assertEquals(cdrEntity.getCredit(), mappedDTO.getCredit());
                assertEquals(cdrEntity.getCreditReferenceId(), mappedDTO.getCreditReferenceId());
                assertEquals(cdrEntity.getLastUpdated(), mappedDTO.getLastUpdated());
        }

        @Test
        void testToListCdrDTO() {
                List<CDR> cdrList = Collections.singletonList(cdrEntity);
                List<CdrDTO> cdrDTOList = CdrMapper.toListCdrDTO(cdrList);
                assertNotNull(cdrDTOList);
                assertEquals(1, cdrDTOList.size());
                assertEquals(cdrEntity.getId(), cdrDTOList.get(0).getId());
        }

        @Test
        void testToListCdrEntity() {
                List<CdrDTO> cdrDTOList = Collections.singletonList(cdrDTO);
                List<CDR> cdrList = CdrMapper.toListCdrEntity(cdrDTOList);
                assertNotNull(cdrList);
                assertEquals(1, cdrList.size());
                assertEquals(cdrDTO.getId(), cdrList.get(0).getId());
        }

        @Test
        void testToListCdrDTO_Null() {
                List<CdrDTO> cdrDTOList = CdrMapper.toListCdrDTO(null);
                assertNull(cdrDTOList);
        }

        @Test
        void testToListCdrEntity_Null() {
                List<CDR> cdrList = CdrMapper.toListCdrEntity(null);
                assertNull(cdrList);
        }
}