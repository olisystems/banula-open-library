package com.banula.openlib.ocpi.model.dto.response;

import com.banula.openlib.ocpi.model.enums.VersionNumber;
import com.banula.openlib.ocpi.model.vo.Endpoint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionDetailsResponseDTO {
    @NotNull
    private VersionNumber version;
    @NotNull
    private List<Endpoint> endpoints;
}
