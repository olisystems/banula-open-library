package com.banula.ocn.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.banula.ocpi.model.vo.Endpoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcnVersionDetails {
    private String version;
    private List<Endpoint> endpoints;
}
