package com.banula.openlib.ocpi.platform;

import com.banula.openlib.ocpi.exception.OCPICustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpMethod;

import java.util.Set;
import java.util.stream.Collectors;

class PlatformClientOcpiComplianceTestHelper {

    private final Validator validator;

    PlatformClientOcpiComplianceTestHelper(Validator validator) {
        this.validator = validator;
    }

    <N> void validateOcpiCompliance(N body, HttpMethod method) {
        if (!HttpMethod.PUT.equals(method)) {
            return;
        }
        if (body == null || !body.getClass().getPackageName().startsWith("com.banula.openlib.ocpi.model.dto")) {
            throw new OCPICustomException(
                    "PUT request body must be a valid OCPI DTO from com.banula.openlib.ocpi.model.dto");
        }
        Set<ConstraintViolation<N>> violations = validator.validate(body);
        if (!violations.isEmpty()) {
            String details = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            throw new OCPICustomException("OCPI object validation failed: " + details);
        }
    }
}
