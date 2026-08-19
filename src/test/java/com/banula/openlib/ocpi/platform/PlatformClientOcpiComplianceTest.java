package com.banula.openlib.ocpi.platform;

import com.banula.openlib.ocpi.exception.OCPICustomException;
import com.banula.openlib.ocpi.model.dto.*;
import com.banula.openlib.ocpi.model.enums.*;
import com.banula.openlib.ocpi.model.vo.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.Assert.*;

public class PlatformClientOcpiComplianceTest {

    private PlatformClientOcpiComplianceTestHelper helper;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        helper = new PlatformClientOcpiComplianceTestHelper(validator);
    }

    // ──────────────────────────────────────────────────────────
    // Non-PUT methods must never validate (no exception expected)
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_nonPutMethod_skipsValidation() {
        Object nonDto = new Object();
        try {
            helper.validateOcpiCompliance(nonDto, HttpMethod.GET);
            helper.validateOcpiCompliance(nonDto, HttpMethod.POST);
            helper.validateOcpiCompliance(nonDto, HttpMethod.PATCH);
            helper.validateOcpiCompliance(nonDto, HttpMethod.DELETE);
        } catch (Exception e) {
            fail("Non-PUT methods should not trigger validation, but got: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────
    // PUT with non-DTO body must throw
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_putWithNonDtoBody_throwsException() {
        try {
            helper.validateOcpiCompliance("not a dto", HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("PUT request body must be a valid OCPI DTO"));
        }
    }

    @Test
    public void validateOcpiCompliance_putWithNullBody_throwsException() {
        try {
            helper.validateOcpiCompliance(null, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("PUT request body must be a valid OCPI DTO"));
        }
    }

    // ──────────────────────────────────────────────────────────
    // LocationDTO
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_validLocationDTO_noException() {
        LocationDTO dto = new LocationDTO();
        dto.setCountryCode("DE");
        dto.setPartyId("ABC");
        dto.setId("LOC001");
        dto.setPublish(true);
        dto.setAddress("Main St 1");
        dto.setCity("Berlin");
        dto.setCountry("DEU");
        dto.setTimeZone("Europe/Berlin");
        dto.setCoordinates(new GeoLocationDTO("52.5200", "13.4050"));
        dto.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
        } catch (Exception e) {
            fail("Valid LocationDTO should not throw, but got: " + e.getMessage());
        }
    }

    @Test
    public void validateOcpiCompliance_locationDTO_missingRequiredFields_throwsException() {
        LocationDTO dto = new LocationDTO();
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("OCPI object validation failed"));
        }
    }

    // ──────────────────────────────────────────────────────────
    // TokenDTO
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_validTokenDTO_noException() {
        TokenDTO dto = new TokenDTO();
        dto.setCountryCode("DE");
        dto.setPartyId("ABC");
        dto.setUid("TOKEN-001");
        dto.setType(TokenType.RFID);
        dto.setContractId("CONTRACT-001");
        dto.setIssuer("OLI");
        dto.setValid(true);
        dto.setWhitelist(WhitelistType.ALWAYS);
        EnergyContract ec = new EnergyContract();
        ec.setSupplierName("GreenEnergy AG");
        dto.setEnergyContract(ec);
        dto.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
        } catch (Exception e) {
            fail("Valid TokenDTO should not throw, but got: " + e.getMessage());
        }
    }

    @Test
    public void validateOcpiCompliance_tokenDTO_missingRequiredFields_throwsException() {
        TokenDTO dto = new TokenDTO();
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("OCPI object validation failed"));
        }
    }

    // ──────────────────────────────────────────────────────────
    // TariffDTO
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_validTariffDTO_noException() {
        TariffDTO dto = new TariffDTO();
        dto.setCurrency("EUR");
        dto.setElements(List.of(new TariffElement(
                List.of(new PriceComponent(TariffDimensionType.FLAT, new BigDecimal("0.5"), 1)))));
        EnergyMix energyMix = new EnergyMix(true);
        dto.setEnergyMix(energyMix);
        dto.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
        } catch (Exception e) {
            fail("Valid TariffDTO should not throw, but got: " + e.getMessage());
        }
    }

    @Test
    public void validateOcpiCompliance_tariffDTO_missingRequiredFields_throwsException() {
        TariffDTO dto = new TariffDTO();
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("OCPI object validation failed"));
        }
    }

    // ──────────────────────────────────────────────────────────
    // CdrDTO
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_validCdrDTO_noException() {
        CdrDTO dto = new CdrDTO();
        dto.setCountryCode("DE");
        dto.setPartyId("ABC");
        dto.setId("CDR-001");
        dto.setStartDateTime(LocalDateTime.now(ZoneOffset.UTC).minusHours(1));
        dto.setEndDateTime(LocalDateTime.now(ZoneOffset.UTC));
        dto.setCdrToken(CdrToken.builder()
                .uid("TOKEN-001")
                .type(TokenType.RFID)
                .contractId("CONTRACT-001")
                .countryCode("DE")
                .partyId("ABC")
                .build());
        dto.setAuthMethod(AuthMethod.AUTH_REQUEST);
        dto.setCdrLocation(CdrLocation.builder()
                .id("LOC001")
                .address("Main St 1")
                .city("Berlin")
                .country("DEU")
                .coordinates(new GeoLocationDTO("52.5200", "13.4050"))
                .connectorStandard(ConnectorType.IEC_62196_T2)
                .connectorFormat(ConnectorFormat.SOCKET)
                .connectorPowerType(PowerType.AC_1_PHASE)
                .build());
        dto.setChargingPeriods(List.of());
        dto.setTotalCost(new Price(0.0f));
        dto.setTotalEnergy(BigDecimal.valueOf(1.0));
        dto.setTotalTime(0.5f);
        dto.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
        } catch (Exception e) {
            fail("Valid CdrDTO should not throw, but got: " + e.getMessage());
        }
    }

    @Test
    public void validateOcpiCompliance_cdrDTO_missingRequiredFields_throwsException() {
        CdrDTO dto = new CdrDTO();
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("OCPI object validation failed"));
        }
    }

    // ──────────────────────────────────────────────────────────
    // EvseDTO
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_validEvseDTO_noException() {
        ConnectorDTO connector = new ConnectorDTO();
        connector.setId("1");
        connector.setStandard(ConnectorType.IEC_62196_T2);
        connector.setFormat(ConnectorFormat.SOCKET);
        connector.setPowerType(PowerType.AC_1_PHASE);
        connector.setMaxVoltage(230);
        connector.setMaxAmperage(16);
        connector.setMaxElectricPower(3680);
        connector.setTariffIds(List.of("TARIFF-001"));
        connector.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));

        EvseDTO dto = new EvseDTO();
        dto.setUid("EVSE-001");
        dto.setEvseId("DE*ABC*E001");
        dto.setStatus(Status.AVAILABLE);
        dto.setConnectors(List.of(connector));
        dto.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
        } catch (Exception e) {
            fail("Valid EvseDTO should not throw, but got: " + e.getMessage());
        }
    }

    @Test
    public void validateOcpiCompliance_evseDTO_missingRequiredFields_throwsException() {
        EvseDTO dto = new EvseDTO();
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("OCPI object validation failed"));
        }
    }

    // ──────────────────────────────────────────────────────────
    // ConnectorDTO
    // ──────────────────────────────────────────────────────────

    @Test
    public void validateOcpiCompliance_validConnectorDTO_noException() {
        ConnectorDTO dto = new ConnectorDTO();
        dto.setId("1");
        dto.setStandard(ConnectorType.IEC_62196_T2);
        dto.setFormat(ConnectorFormat.SOCKET);
        dto.setPowerType(PowerType.AC_1_PHASE);
        dto.setMaxVoltage(230);
        dto.setMaxAmperage(16);
        dto.setMaxElectricPower(3680);
        dto.setTariffIds(List.of("TARIFF-001"));
        dto.setLastUpdated(LocalDateTime.now(ZoneOffset.UTC));
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
        } catch (Exception e) {
            fail("Valid ConnectorDTO should not throw, but got: " + e.getMessage());
        }
    }

    @Test
    public void validateOcpiCompliance_connectorDTO_missingRequiredFields_throwsException() {
        ConnectorDTO dto = new ConnectorDTO();
        try {
            helper.validateOcpiCompliance(dto, HttpMethod.PUT);
            fail("Expected OCPICustomException");
        } catch (OCPICustomException ex) {
            assertTrue(ex.getMessage().contains("OCPI object validation failed"));
        }
    }
}
