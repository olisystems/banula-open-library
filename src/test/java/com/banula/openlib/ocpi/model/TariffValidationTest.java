package com.banula.openlib.ocpi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.banula.openlib.ocpi.model.dto.TariffDTO;
import com.banula.openlib.ocpi.model.enums.TariffDimensionType;
import com.banula.openlib.ocpi.model.vo.Price;
import com.banula.openlib.ocpi.model.vo.PriceComponent;
import com.banula.openlib.ocpi.model.vo.TariffElement;
import com.banula.openlib.ocpi.model.vo.TariffRestrictions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class TariffValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static TariffDTO tariff(PriceComponent component, TariffRestrictions restrictions) {
        TariffElement element = new TariffElement(List.of(component));
        element.setRestrictions(restrictions);
        TariffDTO tariff = new TariffDTO();
        tariff.setCurrency("EUR");
        tariff.setLastUpdated(LocalDateTime.now());
        tariff.setElements(List.of(element));
        return tariff;
    }

    private Set<String> violatedPaths(TariffDTO tariff) {
        return validator.validate(tariff).stream()
                .map(ConstraintViolation::getPropertyPath).map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Test
    void restrictionsWithoutDatesOrTimesAreValid() {
        // OCPI 2.2.1: every TariffRestrictions field is optional.
        TariffRestrictions restrictions = new TariffRestrictions();
        restrictions.setMinKwh(0f);
        restrictions.setMaxDuration(3600);

        assertEquals(Set.of(), violatedPaths(tariff(
                new PriceComponent(TariffDimensionType.ENERGY, new BigDecimal("0.20"), 1, new BigDecimal("19")),
                restrictions)));
    }

    @Test
    void negativePriceComponentValuesAreRejected() {
        Set<String> paths = violatedPaths(tariff(
                new PriceComponent(TariffDimensionType.TIME, new BigDecimal("-1.5"), -60, new BigDecimal("-19")),
                null));

        assertEquals(Set.of("elements[0].priceComponents[0].price", "elements[0].priceComponents[0].vat",
                "elements[0].priceComponents[0].stepSize"), paths);
    }

    @Test
    void negativeRestrictionsAndMinMaxPricesAreRejected() {
        TariffRestrictions restrictions = new TariffRestrictions();
        restrictions.setMinKwh(-1f);
        restrictions.setMaxPower(-22f);
        restrictions.setMaxDuration(-30);
        TariffDTO tariff = tariff(new PriceComponent(TariffDimensionType.ENERGY, new BigDecimal("0.20"), 1),
                restrictions);
        tariff.setMinPrice(new Price(new BigDecimal("-5"), new BigDecimal("-6")));

        Set<String> paths = violatedPaths(tariff);

        assertTrue(paths.containsAll(Set.of("elements[0].restrictions.minKwh", "elements[0].restrictions.maxPower",
                "elements[0].restrictions.maxDuration", "minPrice.exclVat", "minPrice.inclVat")), paths.toString());
        assertEquals(5, paths.size(), paths.toString());
    }

    @Test
    void violationMessagesNameTheOcpiField() {
        Set<String> messages = validator.validate(tariff(
                new PriceComponent(TariffDimensionType.ENERGY, new BigDecimal("-0.20"), 1), null)).stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertEquals(Set.of("price must not be negative"), messages);
    }
}
