package com.banula.openlib.ocpi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StartSessionRequest {
    @NotBlank
    @Size(min = 1, max = 36)
    private String connectorId;
    @NotBlank
    @Size(min = 1, max = 36)
    private String responseUrl;
    @NotBlank
    @Size(min = 1, max = 36)
    private String uid;
}
