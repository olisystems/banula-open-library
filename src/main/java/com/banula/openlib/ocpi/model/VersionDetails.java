package com.banula.openlib.ocpi.model;

import java.util.List;

import com.banula.openlib.ocpi.model.enums.VersionNumber;
import com.banula.openlib.ocpi.model.vo.Endpoint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionDetails {

    /**
     * The version number.
     */
    @JsonProperty("version")
    @Valid // Ensures that the version is valid
    private VersionNumber version;

    /**
     * A list of supported endpoints for this version.
     */
    @JsonProperty("endpoints")
    @NotNull // Ensures that the list of endpoints is not empty
    private List<@Valid Endpoint> endpoints;

}
