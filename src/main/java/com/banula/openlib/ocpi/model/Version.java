package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.openlib.ocpi.model.enums.VersionNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Version {

    /**
     * The version number.
     */
    @JsonProperty("version")
    @NotNull(message = "Version cannot be null.")
    private VersionNumber version;

    /**
     * URL to the endpoint containing version specific information.
     */
    @JsonProperty("url")
    @NotEmpty(message = "URL cannot be empty.")
    @Size(max = 255, message = "URL cannot be longer than 255 characters.")
    private String url;
}
