package com.banula.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.vo.CredentialsRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Credentials {

    /**
     * Case Sensitive, ASCII only. The credentials token for the other party to authenticate in your system.
     * Not encoded in Base64 or any other encoding.
     */
    @NotEmpty(message = "Token must not be empty")
    @Size(min = 1, max = 64, message = "Token length must be between 1 and 64 characters")
    @JsonProperty("token")
    private String token;

    /**
     * The URL to your API versions endpoint.
     */
    @NotEmpty(message = "URL must not be empty")
    @Size(min = 1, max = 255, message = "URL length must be between 1 and 255 characters")
    @JsonProperty("url")
    private String url;

    /**
     * List of the roles this party provides.
     */
    @NotNull(message = "Roles must not be null")
    @JsonProperty("roles")
    private List<CredentialsRole> roles;

}
