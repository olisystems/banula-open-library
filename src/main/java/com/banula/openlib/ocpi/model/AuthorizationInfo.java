package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.AllowedType;
import com.banula.openlib.ocpi.model.vo.DisplayText;
import com.banula.openlib.ocpi.model.vo.LocationReferences;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class AuthorizationInfo {

    @NotNull(message = "Allowed type must not be null")
    @JsonProperty("allowed")
    private AllowedType allowed;

    @NotNull(message = "Token must not be null")
    @JsonProperty("token")
    private Token token;

    @JsonProperty("location")
    private LocationReferences location;

    @NotNull(message = "Authorization reference must not be null")
    @JsonProperty("authorization_reference")
    @Size(max = 36, message = "authorizationReference longer than 36 characters")
    private String authorizationReference;

    @JsonProperty("info")
    private DisplayText info;

}
