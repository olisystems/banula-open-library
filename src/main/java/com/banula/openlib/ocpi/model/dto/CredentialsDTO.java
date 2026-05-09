package com.banula.openlib.ocpi.model.dto;

import java.util.List;

import com.banula.openlib.ocpi.model.vo.CredentialsRole;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDTO {
    @Size(min = 1, max = 64, message = "Token must be between 1 and 64 characters")
    @NotEmpty(message = "Token must not be blank")
    private String token;

    @NotEmpty(message = "URL must not be blank")
    @Size(max = 255, message = "URL must be at most 255 characters long")
    private String url;

    private String credentialsEndpointUrl;

    @NotNull(message = "Roles must not be blank")
    @Valid
    private List<CredentialsRole> roles;
}
