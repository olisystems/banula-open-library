package com.banula.openlib.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.InterfaceRole;
import com.banula.openlib.ocpi.model.enums.ModuleID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {

    /**
     * Endpoint identifier.
     */
    @JsonProperty("identifier")
    @NotNull // Ensures that the identifier is not null
    private ModuleID identifier;

    /**
     * Interface role this endpoint implements.
     */
    @JsonProperty("role")
    @NotNull // Ensures that the role is not null
    private InterfaceRole role;

    /**
     * URL to the endpoint.
     */
    @JsonProperty("url")
    @NotBlank // Ensures that the URL is not blank
    @Size(max = 255) // Ensures that the URL does not exceed 255 characters
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Endpoint endpoint = (Endpoint) o;
        return identifier == endpoint.identifier &&
                role == endpoint.role &&
                url.equals(endpoint.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, role, url);
    }
}
