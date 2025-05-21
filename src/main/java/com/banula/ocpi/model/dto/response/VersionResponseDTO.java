package com.banula.ocpi.model.dto.response;

import com.banula.ocpi.model.enums.VersionNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionResponseDTO {
    @NotNull
    public VersionNumber version;
    @NotBlank
    @Size(max = 255)
    public String url;
}
