package com.banula.ocpi.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.dto.TokenDTO;
import com.banula.ocpi.model.enums.AllowedType;
import com.banula.ocpi.model.vo.DisplayText;
import com.banula.ocpi.model.vo.LocationReferences;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationInfoResponseDTO {
    @NotNull
    @JsonProperty("allowed")
    private AllowedType allowed;
    @NotNull
    @JsonProperty("token")
    private TokenDTO token;
    @JsonProperty("location")
    private LocationReferences location;
    @Size(min = 1, max = 36)
    @JsonProperty("authorization_reference")
    private String authorizationReference;
    @JsonProperty("info")
    private DisplayText info;
}
