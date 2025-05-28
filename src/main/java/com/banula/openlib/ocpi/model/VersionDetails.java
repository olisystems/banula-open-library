package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.VersionNumber;
import com.banula.openlib.ocpi.model.vo.Endpoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
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
